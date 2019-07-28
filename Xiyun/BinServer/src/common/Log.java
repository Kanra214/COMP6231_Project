package BinServer.src.common;

import java.io.IOException;

import java.util.logging.*;

public class Log {
    private Logger LOGGER;
    public Log(String loggerName) {
        this.LOGGER = Logger.getLogger(loggerName.toUpperCase());
        Handler fileHandler = null;
        Formatter simpleFormatter;
        try {
            if (loggerName.matches("\\w*(server)")) {
                fileHandler = new FileHandler("./src/log/server/" + loggerName.toLowerCase() + ".log");
            } else if (loggerName.matches("\\w*(client)")) {
                fileHandler = new FileHandler("./src/log/client/" + loggerName.toLowerCase() + ".log");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);

        this.LOGGER.addHandler(fileHandler);
//        this.LOGGER.info("Logger Name: "+ LOGGER.getName());
    }

    public void info(String type, String[] params, String status) {
            String message = "\nMethodType: " + type + "\n";
            message += "Parameters: " + "\n";
            if (params.length == 5) {
                message += "          CustomerID: " + params[0] + "\n";
                message += "          newEventID: " + params[1] + "\n";
                message += "          newEventType: " + params[2] + "\n";
                message += "          oldEventID: " + params[3] + "\n";
                message += "          oldEventType: " + params[4] + "\n";
            }else {
                if (params[0] != null)
                    message += "          CustomerID: " + params[0] + "\n";
                if (params[1] != null)
                    message += "          EventID: " + params[1] + "\n";
                if (params[2] != null)
                    message += "          EventType: " + params[2] + "\n";
            }

            message += "Status: " + status + "\n";
            LOGGER.info(message);


    }


    public void requestInfo(String type, String[] params) {

            String message = "\nRequest\nMethodType: " + type + "\n";
            message += "Parameters: " + "\n";
             if (params.length == 5) {
            message += "          CustomerID: " + params[0] + "\n";
            message += "          newEventID: " + params[1] + "\n";
            message += "          newEventType: " + params[2] + "\n";
            message += "          oldEventID: " + params[3] + "\n";
            message += "          oldEventType: " + params[4] + "\n";
             }else {
            if (params[0] != null)
                message += "          CustomerID: " + params[0] + "\n";
            if (params[1] != null)
                message += "          EventID: " + params[1] + "\n";
            if (params[2] != null)
                message += "          EventType: " + params[2] + "\n";
            }

            LOGGER.fine(message);

    }
    public Logger getLOGGER() {
        return LOGGER;
    }

    public static void main(String[] args) throws IOException {
//      String date = common.Log.getDate();
//      System.out.println(date);
      Log log = new Log("TORC1111_client");
//      log.write("nihao");

    }
}
