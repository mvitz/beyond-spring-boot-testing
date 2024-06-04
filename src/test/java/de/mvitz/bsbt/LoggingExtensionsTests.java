package de.mvitz.bsbt;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.innoq.junit.jupiter.logging.Logging;
import com.innoq.junit.jupiter.logging.LoggingEvents;
import org.junit.jupiter.api.Test;

import static de.mvitz.bsbt.SomeService.doSomething;
import static org.assertj.core.api.Assertions.assertThat;

// see https://github.com/innoq/junit5-logging-extension for extension implementation
class LoggingExtensionsTests {

    @Test
    void doSomething_shouldLogError_whenMoreThanTwoTries(
        @Logging LoggingEvents events) {
        // when
        doSomething(3);

        // then
        assertThat(events.all())
            .isNotEmpty()
            .extracting(ILoggingEvent::getFormattedMessage)
            .containsExactly("Giving up");
    }
}
