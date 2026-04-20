package wsb.merito.pz.cw04.network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

class Termometer {
    double getTemperature() {
        return 19.5 + Math.random();
    }
}

public class RemoteTermometer extends Termometer {
    RemoteTermometer(String name) throws Exception {
        MulticastSocket ms = new MulticastSocket(10000);
        InetAddress group = InetAddress.getByName("228.222.222.222");
        ms.joinGroup(group);
        while (true) {
            String temp = getTemperature() + " " + name;
            byte[] b = temp.getBytes();
            DatagramPacket out = new DatagramPacket(b, b.length, group, 10000);
            ms.send(out);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new RemoteTermometer("termometer");
    }
}

