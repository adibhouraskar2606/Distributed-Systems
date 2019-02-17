package distribute.HW1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class Answer extends Message {
    private short messageType;
    private short gameID;
    private byte result;
    private short score;
    private String hint;
    private ByteArrayOutputStream outputStream;
    private static Logger logger = Logger.getLogger(String.valueOf(Answer.class));

    public Answer(short messageType, short gameID, byte result, short score, String hint) {
        this.messageType = messageType;
        this.gameID = gameID;
        this.result = result;
        this.score = score;
        this.hint = hint;
    }

    public Answer() {
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

    public byte getResult() {
        return result;
    }

    public void setResult(byte result) {
        this.result = result;
    }

    public short getScore() {
        return score;
    }

    public void setScore(short score) {
        this.score = score;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public ByteBuffer encode() throws IOException {
        return null;
    }

    @Override
    public String decode(ByteBuffer bytes) {
        String resultString = "";
        bytes.order(ByteOrder.BIG_ENDIAN);
        Short msgType = decodeShort(bytes);
        if (msgType==9) {
            Short gameId = decodeShort(bytes);
            String errorMsg = decodeString(bytes);
            resultString = gameId+" "+errorMsg;
            logger.info("Error in Answer encoding"+result);
            return resultString;
        }else {
            logger.info("No error in answer encode");
            Short gameId = decodeShort(bytes);
            byte result = decodeByte(bytes);
            Short score = decodeShort(bytes);
            String hint = decodeString(bytes);
            resultString = "messageType : "+msgType+" GameID : "+gameId+" Result : "+result+" Score : "+score+" Hint : "+hint;
            logger.info("Response after Decoding new game"+"messageType : "+msgType+" GameID : "+gameId+" Result : "+result+" Score : "+score+" Hint : "+hint);

        }
        return resultString;
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
            System.out.println("dxfvghbhjn");
            return null;
        }
        byte[] textBytes = new byte[textLength];
        bytes.get(textBytes, 0, textLength);
        return new String(textBytes, Charset.forName("UTF-16"));
    }
    void encodeShort(short value) throws IOException {
        return;
    }

    void encodeString(String value) throws IOException {
        byte[] textBytes = value.getBytes(Charset.forName("UTF-16"));
        encodeShort((short) textBytes.length);
        outputStream.write(textBytes);
    }
}
