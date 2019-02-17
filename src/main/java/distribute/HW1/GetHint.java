package distribute.HW1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class GetHint extends Message{
    private short messageType;
    private short gameID;
    private ByteArrayOutputStream outputStream;



    public GetHint(short messageType, short gameID) {
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
        return null;
    }
}
