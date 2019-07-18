package client;

import models.Location;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import utils.Constants;
import utils.PropertyManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientManager {
    private Logger logger = null;
    private String eventManagerID;
    CenterServerIdl.CenterServer centerServer = null;
    public String location = "";
    public Location currentLocation;
    private ORB orb;

    public ClientManager(String args[], String eventManagerID) throws Exception {
        this.eventManagerID = eventManagerID;
        orb = ORB.init(args, PropertyManager.getProperties());
        this.centerServer = GetServer(eventManagerID);
        this.initLogger();
    }

    private CenterServerIdl.CenterServer GetServer(String managerId) throws Exception {
        org.omg.CORBA.Object nameServiceRef = orb.resolve_initial_references("NameService");
        NamingContextExt nameService = NamingContextExtHelper.narrow(nameServiceRef);

        //Registry registry = LocateRegistry.getRegistry(Constants.REGISTRY_PORT);
        if (managerId.startsWith("MTL")) {
            this.location = "Montreal Server";
            this.currentLocation = Location.MTL;
            return CenterServerIdl.CenterServerHelper.narrow(nameService.resolve_str("MTLServer"));
        } else if (managerId.startsWith("OTW")) {
            this.location = "Ottawa Server";
            this.currentLocation = Location.OTW;
            return CenterServerIdl.CenterServerHelper.narrow(nameService.resolve_str("OTWServer"));
        } else if (managerId.startsWith("TOR")) {
            this.location = "Toronto Server";
            this.currentLocation = Location.TOR;
            return CenterServerIdl.CenterServerHelper.narrow(nameService.resolve_str("TORServer"));
        }
        return null;
    }

    private void initLogger() throws IOException {
        logger = Logger.getLogger(ClientManager.class.getName() + this.eventManagerID);
        Files.createDirectories(Paths.get(Constants.BACKUP_DIR_NAME, "Client"));
        Path path = Paths.get(Constants.BACKUP_DIR_NAME, "Client", this.eventManagerID + ".log");
        FileHandler fileHandler = new FileHandler(path.toString(), true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.INFO);
    }

    public void addEvent(String eventID, String eventType, int bookingCapacity) {
        try {
            String result = centerServer.addEvent(eventID, eventType, bookingCapacity);
            logger.log(Level.INFO, result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, Constants.ADD_EVENT_FAILURE);
            e.printStackTrace();
        }
    }

    public void bookEvent(String customerID, String eventID, String eventType) {
        try {
            String result = centerServer.bookEvent(customerID, eventID, eventType);
            logger.log(Level.INFO, result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, Constants.BOOK_EVENT_FAILURE);
            e.printStackTrace();
        }
    }

    public void getBookingSchedule(String customerID) {
        try {
            String result = centerServer.getBookingSchedule(customerID);
            logger.log(Level.INFO, result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, Constants.BOOK_SCHEDULE_FAILURE);
            e.printStackTrace();
        }
    }

    public void removeEvent(String eventID, String eventType) {
        try {
            String result = centerServer.removeEvent(eventID, eventType);
            logger.log(Level.INFO, result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, Constants.REMOVE_EVENT_FAILURE);
            e.printStackTrace();
        }
    }

    public void listEventAvailability(String eventType) {
        try {
            String result = centerServer.listEventAvailability(eventType);
            logger.log(Level.INFO, result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, Constants.LIST_EVENT_AVAILABILITY_FAILURE);
            e.printStackTrace();
        }
    }

    public void cancelEvent(String customerID, String eventID) {
        try {
            String result = centerServer.cancelEvent(customerID, eventID);
            logger.log(Level.INFO, result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, Constants.CANCEL_EVENT_FAILURE);
            e.printStackTrace();
        }
    }

    public void swapEvent(String customerID, String newEventID, String newEventType, String oldEventID, String oldEventType) {
        try {
            String result = centerServer.swapEvent(customerID, newEventID, newEventType, oldEventID, oldEventType);
            logger.log(Level.INFO, result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, Constants.SWAP_EVENT_FAILURE);
            e.printStackTrace();
        }
    }
}
