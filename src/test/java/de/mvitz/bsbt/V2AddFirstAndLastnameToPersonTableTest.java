package de.mvitz.bsbt;

import de.mvitz.bsbt.MigrationTest.MigrationTestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@MigrationTest(fromVersion = 1, toVersion = 2)
class V2AddFirstAndLastnameToPersonTableTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void migration_shouldSplitNameIntoFirstAndLastname(MigrationTestTemplate template) {
        template.beforeMigration(() -> {
            jdbcTemplate.execute("TRUNCATE TABLE person");
            jdbcTemplate.execute("INSERT INTO person (name) VALUES ('Test Fixture')");
        });

        template.afterMigration(() -> {
            String person = jdbcTemplate.queryForObject(
                "SELECT * FROM person",
                (rs, rowNum) -> {
                    return rs.getString("lastname") + ", " + rs.getString("firstname");
                });
            assertThat(person)
                .isEqualTo("Fixture, Test");
        });
    }
}
