package spark;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class ResponseStub extends Response {
    private final HttpServletResponseStub httpServletResponseStub = new HttpServletResponseStub();
    private String contentType;
    private final Map<String, String> headers = new HashMap<>();

    private ResponseStub(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void redirect(String location) {
    }

    @Override
    public void type(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public void header(String header, String value) {
        headers.put(header, value);
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    @Override
    public HttpServletResponse raw() {
        return httpServletResponseStub;
    }

    public static class ResponseBuilder {

        public ResponseBuilder() {
        }

        public ResponseStub build() {
            HttpServletResponseWrapper responseWrapper = mock(HttpServletResponseWrapper.class);

            return new ResponseStub(responseWrapper);
        }
    }
}
