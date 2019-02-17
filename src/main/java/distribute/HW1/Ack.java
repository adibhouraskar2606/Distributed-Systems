package distribute.HW1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class  Ack extends Message{
    private short messageType;
    private short gameID;
    private ByteArrayOutputStream outputStream;
    private static Logger logger = Logger.getLogger(String.valueOf(Ack.class));

    public Ack(){

    }

    public Ack(short messageType, short gameID) {
        this.messageType = messageType;
        this.gameID = gameID;
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
        outputStream = new ByteArrayOutputStream();
        encodeShort(messageType);
        encodeShort(gameID);
        return ByteBuffer.wrap(outputStream.toByteArray());
    }
    void encodeShort(short value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort(value);
        outputStream.write(buffer.array());
    }

    void encodeString(String value) throws IOException {
        byte[] textBytes = value.getBytes(Charset.forName("UTF-16"));
        encodeShort((short) textBytes.length);
        outputStream.write(textBytes);
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
            logger.info("Error in new game"+result);
            return result;
        }else {
            System.out.println("No error in Ack game");
            Short gameId = decodeShort(bytes);
            result = "messageType "+msgType+" GameID : "+gameId;
            logger.info("Response after Decoding Ack game"+"Message Type: "+msgType+" Game Id: "+gameId);
        }
        return result;

    }
    private static short decodeShort(ByteBuffer bytes) {
        return bytes.getShort();
    }

    private static String decodeString(ByteBuffer bytes) {
        short textLength = decodeShort(bytes);
        if (bytes.remaining() < textLength) {
            return null;
        }
        byte[] textBytes = new byte[textLength];
        bytes.get(textBytes, 0, textLength);
        return new String(textBytes, Charset.forName("UTF-16"));
    }
}
