package de.mvitz.bsbt;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.sql.ResultSet;

import static java.lang.String.valueOf;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;
import static org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData;
import static org.springframework.test.context.junit.jupiter.SpringExtension.getApplicationContext;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Inherited
@SpringBootTest
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@ExtendWith(MigrationTest.FlywayMigrationTestExtension.class)
public @interface MigrationTest {

    int fromVersion();

    int toVersion();

    class FlywayMigrationTestExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

        @Override
        public void beforeEach(ExtensionContext context) throws Exception {
            // drop all tables
            JdbcTemplate jdbcTemplate = getApplicationContext(context).getBean(JdbcTemplate.class);
            dropAllTables(jdbcTemplate);
        }

        @Override
        public void afterEach(ExtensionContext context) throws Exception {
            // drop all tables and reapply all migrations
            JdbcTemplate jdbcTemplate = getApplicationContext(context).getBean(JdbcTemplate.class);
            dropAllTables(jdbcTemplate);

            DataSource dataSource = jdbcTemplate.getDataSource();

            var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("/db/migration")
                .load();

            flyway.migrate();
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return MigrationTestTemplate.class.equals(parameterContext.getParameter().getType())
                   && findAnnotation(extensionContext.getTestClass(), MigrationTest.class).isPresent();
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return findAnnotation(extensionContext.getTestClass(), MigrationTest.class)
                .map((migrationTest) -> {
                    DataSource dataSource = getApplicationContext(extensionContext).getBean(DataSource.class);
                    int fromVersion = migrationTest.fromVersion();
                    int toVersion = migrationTest.toVersion();

                    return new MigrationTestTemplate(dataSource, fromVersion, toVersion);
                })
                .orElseThrow(() -> new IllegalStateException("unable to create MigrationRestTemplate parameter without @MigrationTest present on test class"));
        }

        private static void dropAllTables(JdbcTemplate jdbcTemplate) throws MetaDataAccessException {
            DataSource dataSource = jdbcTemplate.getDataSource();

            extractDatabaseMetaData(dataSource, (databaseMetaData) -> {
                ResultSet resultSet = databaseMetaData.getTables(null, null, "%", new String[]{"TABLE"});

                while (resultSet.next()) {
                    String tableName = resultSet.getString(3);
                    jdbcTemplate.execute("DROP TABLE \"" + tableName + "\"");
                }

                return null;
            });
        }
    }

    class MigrationTestTemplate {

        private final DataSource dataSource;
        private final int fromVersion;
        private final int toVersion;

        MigrationTestTemplate(DataSource dataSource, int fromVersion, int toVersion) {
            this.dataSource = dataSource;
            this.fromVersion = fromVersion;
            this.toVersion = toVersion;
        }

        public void beforeMigration(Executable executable) {
            try {
                migrateUpTo(fromVersion);
                executable.execute();
            } catch (Throwable throwable) {
                throw new IllegalStateException("unable to execute pre-migration steps", throwable);
            }
        }

        public void afterMigration(Executable executable) {
            try {
                migrateUpTo(toVersion);
                executable.execute();
            } catch (Throwable throwable) {
                throw new IllegalStateException("unable to execute post-migration steps", throwable);
            }
        }

        private void migrateUpTo(int upToVersion) {
            Flyway.configure()
                .dataSource(dataSource)
                .locations("/db/migration")
                .target(valueOf(upToVersion))
                .load()
                .migrate();
        }
    }
}

