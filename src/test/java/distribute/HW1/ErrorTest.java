package distribute.HW1;

import org.junit.Test;

public class ErrorTest {
    Client c = new Client(12001,"127.0.0.1");
    @Test
    public void errorEncodeTest(){
        try{
            c.sendNewGame((short) 1, "A02290685", "bhouraskar", "aditya", "adi");
            System.out.println((c.sendError((short)9)));
        }catch(Exception e){
            e.printStackTrace();
        }

    }


}