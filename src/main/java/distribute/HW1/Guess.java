package distribute.HW1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class Guess extends Message{
    private Short messageType;
    private Short gameID;
    private String guess;
    private ByteArrayOutputStream outputStream;
    static Logger logger = Logger.getLogger(String.valueOf(Guess.class));

    public Guess(short messageType, short gameID, String guess) {
        this.messageType = messageType;
        this.gameID = gameID;
        this.guess = guess;
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

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    @Override
    public ByteBuffer encode() throws IOException {
        outputStream = new ByteArrayOutputStream();
        encodeShort(messageType);
        encodeShort(gameID);
        encodeString(guess);
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
        String resultString = "";
        logger.info("Decoding Guess");
        bytes.order(ByteOrder.BIG_ENDIAN);
        Short msgType = decodeShort(bytes);
        logger.info("Message Type in Guess decode is: "+msgType);

        if (msgType==9){
            Short gameId = decodeShort(bytes);
            String errorMsg = decodeString(bytes);
            resultString = gameId+" "+errorMsg;
            logger.info("Error in guess "+resultString);
            return resultString;
        }else{
            Short gameId = decodeShort(bytes);
            byte result = decodeByte(bytes);
            Short score = decodeShort(bytes);
            String hint = decodeString(bytes);
            resultString = "messageType : "+msgType+" gameId : "+gameId+" result : "+result+" score : "+score+" hint : "+hint;
            logger.info("No error in Guess "+resultString);
            return resultString;
        }
    }
    private static short decodeShort(ByteBuffer bytes) {
        return bytes.getShort();
    }

    private static byte decodeByte(ByteBuffer bytes) {
        return bytes.get();
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
