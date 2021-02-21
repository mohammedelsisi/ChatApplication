package JETS.security;

import JETS.security.enums.HashTypes;
import JETS.security.hash.Hash;
import JETS.security.hash.HashFactory;

import java.util.HashMap;
import java.util.Map;

public class SecurityProvider {
    private static final SecurityProvider SECURITY_PROVIDER = new SecurityProvider();
    private final Map<HashTypes, Hash> hashInstances = new HashMap<>();

    private SecurityProvider() {
    }

    public static SecurityProvider getInstance() {
        return SECURITY_PROVIDER;
    }

    public String hash(HashTypes type, char[] data) {
        Hash hash;
        if (!hashInstances.containsKey(type)) {
            hash = HashFactory.getHash(type);
            hashInstances.put(type, hash);
        } else {
            hash = hashInstances.get(type);
        }
        return hash.hash(data);
    }
}
