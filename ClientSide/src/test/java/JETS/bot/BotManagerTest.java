package JETS.bot;

import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class BotManagerTest {

    @Test
    public void getResponse() {
        BotManager botManager = new BotManager();
        assertNotEquals("I have no answer for that.", botManager.getResponse("Hello"));
    }
}