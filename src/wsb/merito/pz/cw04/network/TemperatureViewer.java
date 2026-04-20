package wsb.merito.pz.cw04.network;

import java.io.IOException;
import java.net.*;

public class TemperatureViewer {
    TemperatureViewer(String ktory) throws Exception {
        MulticastSocket ms = new MulticastSocket(10000);
        InetAddress group = InetAddress.getByName("228.222.222.222");
        InetSocketAddress isa = new InetSocketAddress(group,ms.getLocalPort());
        ms.joinGroup(isa,null);
        byte b[] = new byte[200];
        while (true) {
            DatagramPacket in = new DatagramPacket(b, b.length);
            try {
                ms.receive(in);
            } catch (IOException e) {
            }
            String s = new String(in.getData(), 0, in.getLength());
            System.out.println("Temperature: " + ktory + " " + s);
        }
    }

    public static void main(String args[]) throws Exception {
        //new TemperatureViewer("pierwszy");
        new TemperatureViewer("drugi");

    }
}

