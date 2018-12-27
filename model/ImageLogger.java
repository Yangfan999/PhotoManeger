package model;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logs image file's name change
 * @author QiZeng, yangfanli, yuyuexin
 * @version IntelliJ IDEA 2017.2.4
 */
class ImageLogger {
    private Logger logger;

    /**
     * Initiate a new ImageLogger, saves log into ImageLog.txt
     * @param class_name logger name
     */
    ImageLogger(String class_name) {
        logger = Logger.getLogger(class_name);
        try {
            FileHandler handler = new FileHandler("ImageLog.txt", true);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
            logger.addHandler(handler);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Logs message
     * @param message: message about the changed image file name
     */
    void log(String message) {
        logger.info(message);
    }
}
