package wsb.merito.pz.cw04.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer2 {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(12129);
            socket = serverSocket.accept();
        } catch (Exception e) {
            System.err.println("Create server socket: " + e);
            return;
        }
        while (true) {
            try {
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                String fromClient = br.readLine();
                System.out.println("From client: [" + fromClient + "]");
                pw.println("Echo: " + fromClient);
                if (fromClient == null || fromClient.equals(".bye")) {
                    socket.close();
                    break;
                }
            } catch (Exception e) {
                System.err.println("Server exception: " + e);
            }
        }
    }
}

