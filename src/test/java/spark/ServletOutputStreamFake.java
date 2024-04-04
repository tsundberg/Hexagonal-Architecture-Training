package spark;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;

public class ServletOutputStreamFake extends ServletOutputStream {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    ServletOutputStreamFake() {
    }

    @Override
    public boolean isReady() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(int b) {
        outputStream.write(b);
    }
}
