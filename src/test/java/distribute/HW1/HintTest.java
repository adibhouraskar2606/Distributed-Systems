package distribute.HW1;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class HintTest {
    Client c = new Client(12001,"127.0.0.1");
    @Test
    public void encodeTest() {
        try {
            GetHint getHint = new GetHint((short) 5, (short)20);
            ByteBuffer buffer = getHint.encode();
            assertEquals(4,c.getClient().send(buffer,c.getServerAddress()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}