package distribute.HW1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class HeartBeat extends Message{
    private short messageType;
    private short gameID;
    private ByteArrayOutputStream outputStream;
    static Logger logger = Logger.getLogger(String.valueOf(HeartBeat.class));

    public HeartBeat(short messageType, short gameID) {
        this.messageType = messageType;
        this.gameID = gameID;
    }
    public HeartBeat(){

    }

    public short getMessageType() {
        return messageType;
    }

    public void setMessageType(short messageType) {
        this.messageType = messageType;
    }

    public short getGameID() {
        return gameID;
    }

    public void setGameID(short gameID) {
        this.gameID = gameID;
    }

    @Override
    public ByteBuffer encode() throws IOException {
        return null;
    }

    @Override
    public String decode(ByteBuffer bytes) {
        String result = "";
        bytes.order(ByteOrder.BIG_ENDIAN);
        Short msgType = decodeShort(bytes);
        if (msgType==9) {
            Short gameId = decodeShort(bytes);
            String errorMsg = decodeString(bytes);
            result = gameId+" "+errorMsg;
            logger.info("Error in Heartbeat "+result);
            return result;
        }else {
            System.out.println("No error in Heartbeat class");
            Short gameId = decodeShort(bytes);
            result = "messageType : "+msgType+" GameID : "+gameId;
            logger.info("Response after Decoding Heartbeat "+"Message Type: "+msgType+" Game Id: "+gameId);
        }
        return result;
    }
    void encodeShort(short value) throws IOException{

    }
    void encodeString(String value) throws IOException{

    }
    private static short decodeShort(ByteBuffer bytes) {
        return bytes.getShort();
    }

    private static String decodeString(ByteBuffer bytes) {
        short textLength = decodeShort(bytes);
        if (bytes.remaining() < textLength) {
            System.out.println("dxfvghbhjn");
            return null;
        }
        byte[] textBytes = new byte[textLength];
        bytes.get(textBytes, 0, textLength);
        return new String(textBytes, Charset.forName("UTF-16"));
    }
}
