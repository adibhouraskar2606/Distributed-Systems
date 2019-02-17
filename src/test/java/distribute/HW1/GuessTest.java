package distribute.HW1;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class GuessTest {
    Client c = new Client(12001,"127.0.0.1");
    @Test
    public void guessCreationTest() throws Exception{
        c.sendNewGame((short) 1, "A02290685", "bhouraskar", "aditya", "adi");
        String ab = c.sendGuess((short)3,"abc");
        assertEquals(ab,c.sendGuess((short)3,"abc"));
    }
    @Test
    public void encodeTest() {
        try {
            Guess guess = new Guess((short) 3, (short)20, "abc");
            ByteBuffer buffer = guess.encode();
            assertEquals(14,c.getClient().send(buffer,c.getServerAddress()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}