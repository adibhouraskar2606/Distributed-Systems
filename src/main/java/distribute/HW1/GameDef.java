package distribute.HW1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class GameDef extends Message{
    private short messageType;
    private short gameID;
    private String hint;
    private String definition;
    private ByteArrayOutputStream outputStream;
    static Logger logger = Logger.getLogger(String.valueOf(Exit.class));

    public GameDef(short messageType, short gameID, String hint, String definition) {
        this.messageType = messageType;
        this.gameID = gameID;
        this.hint = hint;
        this.definition = definition;
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

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public ByteBuffer encode() throws IOException {
        outputStream = new ByteArrayOutputStream();
        encodeShort(messageType);
        encodeShort(gameID);
        encodeString(hint);
        encodeString(definition);
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
        Short messageType = decodeShort(bytes);
        Short gameID = decodeShort(bytes);
        String hint = decodeString(bytes);
        String def = decodeString(bytes);
        logger.info("Response after Decoding "+"Message Type: "+messageType+" Game Id: "+gameID+" Hint: "+hint+" Definition: "+def);
        return gameID+" "+def;

//    return null;
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
