package distribute.HW1;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class ExitTest {
    Client c = new Client(12001,"127.0.0.1");
@Test
public void guessCreationTest() throws Exception{
        c.sendNewGame((short) 1, "A02290685", "bhouraskar", "aditya", "adi");
        c.sendGuess((short)3,"abc");
        String ab = c.sendExit((short)7);
        assertEquals(ab,c.sendExit((short)7));
        }
    @Test
    public void encodeTest() {
        try {
            Exit exit = new Exit((short)7,(short)20);
            ByteBuffer buffer = exit.encode();
            assertEquals(4,c.getClient().send(buffer,c.getServerAddress()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}