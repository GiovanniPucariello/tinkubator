package gov.lanl.cnls.linkedprocess.xmpp;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import gov.lanl.cnls.linkedprocess.GenericPacketListener;

/**
 * Created by IntelliJ IDEA.
 * User: marko
 * Date: Jun 23, 2009
 * Time: 11:01:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class LOPClient {

        // this is a development and testing account. please don't use for any other purposes.
    private static String username = "linked.process.1@gmail.com";
    private static String password = "linked12";
    private static String resource = "lop/";
    private static String xmpp_server = "talk1.l.google.com";
    private static int xmpp_port = 5222;

    public static void main(String[] args) throws Exception {
        new LOPClient();
    }

    public LOPClient() throws Exception {

        System.out.println("Starting client");
        ConnectionConfiguration connConfig = new ConnectionConfiguration(xmpp_server, xmpp_port);
        XMPPConnection connection = new XMPPConnection(connConfig);

        // making a connection to an XMPP server is different than logging into an XMPP server
        try {
            connection.connect();
            System.out.println("Connected to " + connection.getHost());
        } catch (XMPPException ex) {
            System.out.println("Failed to connect to " + connection.getHost());
			System.exit(1);
        }
        // logging into an XMPP server requires a username and password
        try {
            connection.login(username, password, resource);
            System.out.println("Logged in as " + connection.getUser());
            // acknowledge to all "buddies" your presence. This presence uses a custom message and a priority value.
            //Presence presence = new Presence(Presence.Type.available,
            //        "Waiting to process...", 24, Presence.Mode.available);
            //connection.sendPacket(presence);

        } catch (XMPPException ex) {
            System.out.println("Failed to log in as " + username);
            System.out.println(ex);
			System.exit(1);
        }

        // print a collection of statistics about the connection
        System.out.println();
        System.out.println("Anonymous: " + connection.isAnonymous());
        System.out.println("Authenticated: " + connection.isAuthenticated());
        System.out.println("Connected: " + connection.isConnected());
        System.out.println("Secure: " + connection.isSecureConnection());
        System.out.println("Compression: " + connection.isUsingCompression());
        System.out.println("Transport Layer Security: " + connection.isUsingTLS());
        System.out.println();


        // Smack uses a listener framework to receive packets/stanzas and perform operations on packets.
        // A listener can have a filter to only handle certain types of packets (tags/attributes)
        //PacketFilter filter = new PacketTypeFilter(IQ.class);
        connection.addPacketListener(new GenericPacketListener(), null);

    }
}
