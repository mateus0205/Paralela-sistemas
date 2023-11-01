import java.net.*;
import java.io.*;

public class TCPEchoServer {
    private static final int BUFSIZE = 32; // Size of receive buffer

    public static void main(String[] args) throws IOException {
        if (args.length != 1)
            throw new IllegalArgumentException("Parameter(s): <Port>");
        try {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("Local Host:");
            System.out.println("\t" + address.getHostName());
            System.out.println("\t" + address.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Unable to determine this host's address");
        }

        int servPort = Integer.parseInt(args[0]);
        ServerSocket servSock = null; // Declare ServerSocket outside the try block
        try {
            servSock = new ServerSocket(servPort); // Create a server socket to accept client connection requests
            int recvMsgSize; // Size of receive message
            byte[] byteBuffer = new byte[BUFSIZE]; // Receive buffer
            for (;;) { // Run forever, accepting and servicing connections
                Socket clntSock = servSock.accept(); // Get client connection
                System.out.println("Handling client at " +
                        clntSock.getInetAddress().getHostAddress() + " on port " +
                        clntSock.getPort());
                InputStream in = clntSock.getInputStream();
                OutputStream out = clntSock.getOutputStream();
                // Receive until the client closes the connection, indicated by -1 return
                while ((recvMsgSize = in.read(byteBuffer)) != -1)
                    out.write(byteBuffer, 0, recvMsgSize);
                clntSock.close(); // Close the socket. We are done with this client.
            }
            /* NOT REACHED */
        } finally {
            if (servSock != null && !servSock.isClosed()) {
                servSock.close(); // Close the ServerSocket in the finally block
            }
        }
    }
}
