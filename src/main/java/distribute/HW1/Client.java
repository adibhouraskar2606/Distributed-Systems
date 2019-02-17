package distribute.HW1;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread{
    private DatagramSocket clientSocket;
    private InetAddress ipAddress;
    private int port;
    private String host;
    private DatagramChannel client = null;
    static DatagramChannel newClient;
    private ByteBuffer buffer;
    private Short gameID = null;
    private int currentBufferSize = 0;
    private InetSocketAddress serverAddress;
    private static Logger logger = Logger.getLogger(String.valueOf(Client.class));
    public void run(){
        try {
//            Thread.sleep(4000);
            logger.info("Heartbeat reading");
            readHeartBeat(Client.newClient);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Client(){

    }
    public Client(int port, String host) {
        try {
            this.port = port;
            this.host = host;
            this.buffer = ByteBuffer.allocate(1024);
            this.ipAddress = InetAddress.getByName(host);
            this.serverAddress = new InetSocketAddress(ipAddress, port);
            clientSocket = new DatagramSocket();
            client = DatagramChannel.open();
            client.bind(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    InetSocketAddress getServerAddress() {
        return serverAddress;
    }

    DatagramChannel getClient() {
        return client;
    }

    String sendNewGame(short messageType, String aNumber, String lastName, String firstName, String alias){
        String msgType = "";
        try {
            NewGame msgObj = new NewGame(messageType, aNumber, lastName, firstName, alias);
            logger.info("new game request sent to server");
            buffer = msgObj.encode();
            DatagramChannel client = null;
            InetSocketAddress serverAddress = new InetSocketAddress(ipAddress, port);
            System.out.println("Port is : "+port);
            client = DatagramChannel.open();
            client.bind(null);
            currentBufferSize = client.send(buffer, serverAddress);
            if(buffer==null){
                logger.warning("BufferUnderFlow");
                throw new Exception("cant connect to sever");
            }
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, "Message Sent to Server");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.receive(buffer);
            buffer.flip();
            System.out.println("Client "+client);
            Client.newClient = client;
            String resObj = msgObj.decode(buffer);
            String[] brokenString = resObj.split(" ");
            msgType = brokenString[2];
            gameID = Short.valueOf(brokenString[5]);

            Thread t = new Thread(new Client());
            t.start();

        }catch (Exception e){
            e.printStackTrace();
            logger.warning("error in send new game");
        }
        return msgType;
    }

    int getCurrentBufferSize() {
        return currentBufferSize;
    }

    public Short getGameID() {
        return gameID;
    }

    String sendGuess(Short messageType, String guess){
        String msgType = "";
        try {
            Guess msgObj = new Guess(messageType, gameID,  guess);
            buffer = msgObj.encode();
            DatagramChannel client = null;
            InetSocketAddress serverAddress = new InetSocketAddress(ipAddress, port);
            client = DatagramChannel.open();
            client.bind(null);
            client.send(buffer, serverAddress);
            logger.info("guess message sent to server");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.receive(buffer);
            buffer.flip();
            String resObj = msgObj.decode(buffer);
            logger.info("send guess server returns message "+resObj);
            String [] brokenString = resObj.split(" ");
            msgType = brokenString[2];
        }catch (Exception e){
            e.printStackTrace();
            logger.info("error in send guess method");
        }
        return msgType;
    }
    String sendError(Short messageType) throws Exception{
        Error msgObj = new Error(messageType, gameID);
        String resObj = msgObj.decode(buffer);
        logger.info("error decode "+resObj);
        return resObj;
    }
    void sendHint(Short messageType){
        try{
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, "Get Hint starts ");
            GetHint msgObj = new GetHint(messageType, gameID);
            buffer = msgObj.encode();
            DatagramChannel client = null;
            InetSocketAddress serverAddress = new InetSocketAddress(ipAddress, port);
            client = DatagramChannel.open();
            client.bind(null);
            client.send(buffer, serverAddress);
            logger.info("message sent to server to getHint");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.receive(buffer);
            buffer.flip();
            Hint retObj = new Hint((short)6,gameID);
            String resObj = retObj.decode(buffer);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("error in send hint method");
        }
    }
    String sendExit(Short messageType){
        String msgType = "";
        try{
            logger.info("exit method starts");
            Exit msgObj = new Exit(messageType, gameID);
            buffer = msgObj.encode();
            DatagramChannel client = null;
            InetSocketAddress serverAddress = new InetSocketAddress(ipAddress, port);
            client = DatagramChannel.open();
            client.bind(null);
            client.send(buffer, serverAddress);
            logger.info("exit message sent to server");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.receive(buffer);
            buffer.flip();
            Ack retObj = new Ack((short)8,gameID);
            String resObj = retObj.decode(buffer);
            logger.info("Acknowledgement sent from decode "+resObj);
            String [] brokenText = resObj.split(" ");
            msgType = brokenText[1];
        }catch (Exception e){
            e.printStackTrace();
        }
        return msgType;
    }
    public void readHeartBeat(DatagramChannel channel) throws Exception{
        logger.info("heart beat read method "+channel);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.receive(buffer);
        buffer.flip();
        new HeartBeat().decode(buffer);
        System.out.println("reading");
//        Ack msgObj = new Ack((short)8,gameID);
//
//        msgObj.encode();
//        channel.send(buffer,serverAddress);
    }
}

