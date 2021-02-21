package JETS.security.hash;

import JETS.security.enums.HashTypes;

public class HashFactory {
    private HashFactory() {
    }

    public static Hash getHash(HashTypes type){
        Hash hash = null;
        switch (type){
            case SHA3:
                hash = new SHA3Hash();
                break;
        }
        return hash;
    }

}
