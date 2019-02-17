package distribute.HW1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class NewGame extends Message{
    private short messageType;
    private String aNumber;
    private String lastName;
    private String firstName;
    private String alias;
    private ByteArrayOutputStream outputStream;
    private static Logger logger = Logger.getLogger(String.valueOf(Client.class));

    public short getMessageType() {
        return messageType;
    }

    public void setMessageType(short messageType) {
        this.messageType = messageType;
    }

    public String getaNumber() {
        return aNumber;
    }

    public void setaNumber(String aNumber) {
        this.aNumber = aNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public NewGame(short messageType, String aNumber, String lastName, String firstName, String alias){
        this.messageType = messageType;
        this.aNumber = aNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.alias = alias;
    }
    public NewGame(){

    }
    @Override
    public ByteBuffer encode() throws IOException {
        outputStream = new ByteArrayOutputStream();
        encodeShort(messageType);
        encodeString(aNumber);
        encodeString(lastName);
        encodeString(firstName);
        encodeString(alias);
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
                logger.info("No error in new game");
                Short gameId = decodeShort(bytes);
                String hint = decodeString(bytes);
                String def = decodeString(bytes);
                result = "messageType : "+msgType+" GameID : "+gameId+" Hint : "+hint+" Definition : "+def;
                logger.info("Response after Decoding new game"+"Message Type: "+msgType+" Game Id: "+gameId+" Hint: "+hint+" Definition: "+def);

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
