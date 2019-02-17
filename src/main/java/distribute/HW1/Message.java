package distribute.HW1;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class Message{
    public abstract ByteBuffer encode() throws IOException;
    public abstract String decode(ByteBuffer bytes);
    abstract void encodeShort(short value) throws IOException;
    abstract void encodeString(String value) throws IOException;
}