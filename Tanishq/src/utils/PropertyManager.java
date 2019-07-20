package utils;

import java.util.Properties;

public class PropertyManager {
    private static Properties properties = System.getProperties();

    static {
        properties.put("org.omg.CORBA.ORBInitialPort", Integer.toString(Constants.ORB_INITIAL_PORT));
        properties.put("org.omg.CORBA.ORBInitialHost", Constants.ORB_INITIAL_HOST);
    }

    public static Properties getProperties() {
        return properties;
    }
}