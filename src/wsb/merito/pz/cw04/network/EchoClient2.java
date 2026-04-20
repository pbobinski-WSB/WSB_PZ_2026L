package wsb.merito.pz.cw04.network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient2 {
    public static void main(String args[]) {
        try {
            Socket socket = new Socket("localhost", 12129);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            while (line != null && !line.equals(".bye")) {
                pw.println(line);
                System.out.println(br.readLine());
                line = sc.nextLine();
            }
            socket.close();
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
        }
    }
}

