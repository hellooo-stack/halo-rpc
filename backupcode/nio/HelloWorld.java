package site.hellooo.rpc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
    public static void main(String[] args) {
//
//        Logger logger = LoggerFactory.getLogger("chapters.introduction.HelloWorld1");
        Logger logger = LoggerFactory.getLogger(HelloWorld.class);
        logger.debug("Hello world.");

//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        StatusPrinter.print(lc);

        Logger rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        System.out.println(rootLogger.getName());

//        t1();
    }

    public static void t1() {
        // get a logger instance named "com.foo". Let us further assume that the
// logger is of type  ch.qos.logback.classic.Logger so that we can
// set its level
        ch.qos.logback.classic.Logger logger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.foo");
//set its Level to INFO. The setLevel() method requires a logback logger
        logger.setLevel(Level.INFO);

        Logger barlogger = LoggerFactory.getLogger("com.foo.Bar");

// This request is enabled, because WARN >= INFO
        logger.warn("Low fuel level.");

// This request is disabled, because DEBUG < INFO.
        logger.debug("Starting search for nearest gas station.");

// The logger instance barlogger, named "com.foo.Bar",
// will inherit its level from the logger named
// "com.foo" Thus, the following request is enabled
// because INFO >= INFO.
        barlogger.info("Located nearest gas station.");

// This request is disabled, because DEBUG < INFO.
        barlogger.debug("Exiting gas station search");
    }
}
