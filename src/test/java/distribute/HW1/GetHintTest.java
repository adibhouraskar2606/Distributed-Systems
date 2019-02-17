package distribute.HW1;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class GetHintTest {
    Client c = new Client(12001,"127.0.0.1");
    @Test
    public void encodeTest() {
        try {
            c.sendNewGame((short)1, "A02290685", "bhouraskar", "aditya", "adi");
            GetHint getHint = new GetHint((short) 5, c.getGameID());
            ByteBuffer buffer = getHint.encode();
            Hint hint = new Hint((short) 6, c.getGameID());
            System.out.println(hint.decode(buffer));
            assertEquals(4,c.getClient().send(buffer,c.getServerAddress()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}