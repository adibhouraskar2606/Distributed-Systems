package distribute.HW1;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClientTest {
    Client c = new Client(12001,"127.0.0.1");
    @Test
    public void newGameTest(){
        try{
           String a = c.sendNewGame((short)1, "A02290685", "bhouraskar", "aditya", "adi");
           assertEquals("2",a);
    }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void gameIDTest(){

    }
}
