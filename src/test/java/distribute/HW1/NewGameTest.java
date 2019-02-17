package distribute.HW1;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class NewGameTest {
    Client c = new Client(12001, "127.0.0.1");

    @Test
    public void bufferTest() throws Exception {
        c.sendNewGame((short) 1, "A02290685", "bhouraskar", "aditya", "adi");
        assertEquals(74, c.getCurrentBufferSize());
    }

    @Test
    public void newGameCreationTest() throws Exception {
        String a = c.sendNewGame((short) 1, "A02290685", "bhouraskar", "aditya", "adi");
        assertEquals("2", a);
    }

    @Test
    public void encodeTest() {
        try {
            NewGame newGame = new NewGame((short) 1, "A02290685", "bhouraskar", "aditya", "adi");
            ByteBuffer buffer = newGame.encode();
            assertEquals(74, c.getClient().send(buffer, c.getServerAddress()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void nameTest() {
        try {
            NewGame newGame = new NewGame((short) 1, "A02290685", "bhouraskar", "aditya", "adi");
            ByteBuffer buffer = newGame.encode();
            assertEquals("bhouraskar", newGame.getLastName());
            assertEquals("aditya", newGame.getFirstName());
            assertEquals("adi", newGame.getAlias());
            assertEquals("A02290685", newGame.getaNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}