package de.mvitz.bsbt;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.time.ZoneOffset.UTC;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;
import static org.springframework.test.context.junit.jupiter.SpringExtension.getApplicationContext;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Inherited
@ExtendWith(WithLocalDateTime.WithLocalDateTimeExtension.class)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
@ImportAutoConfiguration(WithLocalDateTime.ClockConfiguration.class)
public @interface WithLocalDateTime {

    String date();
    String time();

    @TestConfiguration
    class ClockConfiguration {

        @Bean
        public Clock clock() {
            return new DelegatingClock(Clock.systemUTC());
        }
    }

    class WithLocalDateTimeExtension implements BeforeEachCallback, AfterEachCallback {

        private static final Namespace EXTENSION_SCOPE = Namespace.create(WithLocalDateTimeExtension.class);
        private static final Class<DelegatingClock> DELEGATING_CLOCK_KEY = DelegatingClock.class;

        @Override
        public void beforeEach(ExtensionContext extensionContext) {
            findAnnotation(extensionContext.getTestClass(), WithLocalDateTime.class)
                .ifPresent((withLocalDateTime) -> setClockTo(extensionContext, withLocalDateTime));
        }

        private static void setClockTo(ExtensionContext extensionContext, WithLocalDateTime withLocalDateTime) {
            DelegatingClock delegatingClock = delegatingClockFrom(extensionContext);

            Clock delegate = delegatingClock.getDelegate();
            extensionContext.getStore(EXTENSION_SCOPE).put(DELEGATING_CLOCK_KEY, delegate);

            LocalDateTime localDateTime = localDateTimeFrom(withLocalDateTime);
            delegatingClock.setDelegate(fixedClock(localDateTime));
        }

        private static DelegatingClock delegatingClockFrom(ExtensionContext context) {
            Clock clock = getApplicationContext(context).getBean(Clock.class);

            if (!(clock instanceof DelegatingClock)) {
                throw new IllegalStateException("clock '" + clock + "' must be of type '" + DelegatingClock.class.getName() + "'");
            }

            return (DelegatingClock) clock;
        }

        private static Clock fixedClock(LocalDateTime localDateTime) {
            return Clock.fixed(localDateTime.atZone(UTC).toInstant(), UTC);
        }

        @Override
        public void afterEach(ExtensionContext extensionContext) {
            findAnnotation(extensionContext.getTestClass(), WithLocalDateTime.class)
                .ifPresent((withLocalDateTime) -> resetClockFrom(extensionContext));
        }

        private static void resetClockFrom(ExtensionContext extensionContext) {
            DelegatingClock delegatingClock = delegatingClockFrom(extensionContext);

            Clock delegate = (Clock) extensionContext.getStore(EXTENSION_SCOPE).remove(DELEGATING_CLOCK_KEY);

            delegatingClock.setDelegate(delegate);
        }

        private static LocalDateTime localDateTimeFrom(WithLocalDateTime withLocalDateTime) {
            return LocalDateTime.of(
                LocalDate.parse(withLocalDateTime.date(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalTime.parse(withLocalDateTime.time(), DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }

    class DelegatingClock extends Clock {

        private Clock delegate;

        DelegatingClock(Clock delegate) {
            this.delegate = delegate;
        }

        Clock getDelegate() {
            return delegate;
        }

        void setDelegate(Clock delegate) {
            this.delegate = delegate;
        }

        @Override
        public ZoneId getZone() {
            return delegate.getZone();
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return delegate.withZone(zone);
        }

        @Override
        public Instant instant() {
            return delegate.instant();
        }
    }
}

