package de.mvitz.bsbt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SomeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SomeService.class);

    static void doSomething(int tries) {
        try {
            // ...
            throw new Exception("Some error");
        } catch (Exception e) {
            if (tries > 2) {
                // ...
                LOGGER.error("Giving up");
            } else {
                // ...
                LOGGER.warn("Failure, trying again later");
            }
        }
    }
}
