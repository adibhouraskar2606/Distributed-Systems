package distribute.HW1;
import org.apache.log4j.BasicConfigurator;

import java.util.logging.Logger;

public class MainClass {
    static Logger logger = Logger.getLogger(String.valueOf(MainClass.class));
    public static void main(String gg[]) throws Exception {
        BasicConfigurator.configure();
        Client client = new Client(12001, "127.0.0.1");
//        Thread t = new Thread(client);
//        t.start();
        String a = client.sendNewGame((short) 1, "A02290685", "bhouraskar", "aditya", "adi");
        logger.info("Guess message begins");
        if (a.equals("2")) {
            client.sendGuess((short) 3, "xyz");
        }
        client.sendHint((short) 5);
        client.sendExit((short) 7);
    }
}
