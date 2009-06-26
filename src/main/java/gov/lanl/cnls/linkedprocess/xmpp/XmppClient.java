package gov.lanl.cnls.linkedprocess.xmpp;

import gov.lanl.cnls.linkedprocess.LinkedProcess;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import java.util.Iterator;

/**
 * User: marko
 * Date: Jun 25, 2009
 * Time: 11:31:34 AM
 */
public abstract class XmppClient {

    public static Logger LOGGER = LinkedProcess.getLogger(XmppClient.class);
    protected XMPPConnection connection;
    protected boolean shutdownRequested = false;

    private String username;
    private String password;
    private String server;
    private int port;

    protected void initiateFeatures() {
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
        Iterator<String> features = discoManager.getFeatures();
        while (features.hasNext()) {
            String feature = features.next();
            discoManager.removeFeature(feature);
        }
        discoManager.addFeature(LinkedProcess.LOP_NAMESPACE);
        discoManager.addFeature(LinkedProcess.DISCO_INFO_NAMESPACE);
    }

    protected void logon(String server, int port, String username, String password, String resource) throws XMPPException {

        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;

        // if connection is still active, disconnect it.
        if (null != connection && connection.isConnected()) {
            this.logout();
        }

        // logging into an XMPP server requires a username and password
        ConnectionConfiguration connConfig = new ConnectionConfiguration(server, port);
        this.connection = new XMPPConnection(connConfig);
        this.connection.connect();

        LOGGER.info("Connected to " + connection.getHost());
        connection.login(username, password, resource);
        LOGGER.info("Logged in as " + connection.getUser());
  
        Thread shutdownHook = new Thread(new Runnable() {
            public void run() {
                try {
                    while (!shutdownRequested) {
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LOGGER.info("Shutting down");
            }
        });
        shutdownHook.start();
    }

    public void logout() {
        LOGGER.info("Disconnecting from " + connection.getHost());
        connection.disconnect(new Presence(Presence.Type.unavailable));
        connection.disconnect();
    }

    public void printClientStatistics() {
        // print a collection of statistics about the connection
        LOGGER.info("Anonymous: " + connection.isAnonymous());
        LOGGER.info("Authenticated: " + connection.isAuthenticated());
        LOGGER.info("Connected: " + connection.isConnected());
        LOGGER.info("Secure: " + connection.isSecureConnection());
        LOGGER.info("Compression: " + connection.isUsingCompression());
        LOGGER.info("Transport Layer Security: " + connection.isUsingTLS());
    }

    public String getFullJid() {
        return this.connection.getUser();
    }

    public XMPPConnection getConnection() {
        return this.connection;
    }

    public void shutDown() {
        LOGGER.info("Requesting shutdown");
        shutdownRequested = true;
        logout();
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPort() {
        return this.port;
    }

    public String getServer() {
        return this.server;
    }
}
