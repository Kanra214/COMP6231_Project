package utils;

import models.Location;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtility {
    public static Logger getLogger(Location location) throws IOException {
        Logger logger = Logger.getLogger(location.toString());
        Files.createDirectories(Paths.get(Constants.LOG_DIR, location.toString()));
        Path path = Paths.get(Constants.LOG_DIR, location.toString(), "logger.log");
        FileHandler fHandler = new FileHandler(path.toString(), true);
        fHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fHandler);
        logger.setLevel(Level.INFO);
        return logger;
    }
}
