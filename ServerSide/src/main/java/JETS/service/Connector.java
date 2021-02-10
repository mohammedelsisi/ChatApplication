package JETS.service;

import Services.ClientServices;

public interface Connector {
    boolean isConnected(String clientPhoneNumber);
    ClientServices getClientService (String clientPhoneNumber );
}
