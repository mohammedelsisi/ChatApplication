package JETS;

import JETS.net.ClientProxy;
public class ClientDemo {
    public static String ipOfserver;
    public static void main(String[] args) {
       ipOfserver = args[0];
        ClientMain.main(args);
    }
}
