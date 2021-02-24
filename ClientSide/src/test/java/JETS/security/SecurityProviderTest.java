package JETS.security;

import JETS.security.enums.HashTypes;
import org.junit.Test;

import static org.junit.Assert.*;

public class SecurityProviderTest {

    @Test
    public void hashViaSHA3() {
        assertEquals("1d6442ddcfd9db1ff81df77cbefcd5afcc8c7ca952ab3101ede17a84b866d3f3",SecurityProvider.getInstance().hash(HashTypes.SHA3, "1234".toCharArray()));
    }
}