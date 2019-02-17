package distribute.HW1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.logging.Logger;


public class Error extends Message{
    private short messageType;
    private short gameID;
    private String errorText;
    private static Logger logger = Logger.getLogger(String.valueOf(Error.class));
    private ByteArrayOutputStream outputStream;

    public Error(short messageType, short gameID) {
        this.messageType = messageType;
        this.gameID = gameID;
        errorText = "";
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

    public String getErrorText() {
        return errorText;
    }

    @Override
    public ByteBuffer encode() throws IOException {
        outputStream = new ByteArrayOutputStream();
        encodeShort(messageType);
        encodeShort(gameID);
        encodeString(errorText);
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
        bytes.order(ByteOrder.BIG_ENDIAN);
        logger.info("decode of error");
        Short messageType = decodeShort(bytes);
        Short gameID = decodeShort(bytes);
        String errorText = decodeString(bytes);
        logger.info("Response after Decoding "+"Message Type: "+messageType+" Game Id: "+gameID+" errortext: "+errorText);
        return gameID+" "+errorText;
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
