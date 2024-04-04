package spark;


import se.thinkcode.infrastructure.Serializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.util.*;

import static org.mockito.Mockito.mock;

public class RequestStub extends Request {
    private Map<String, List<String>> queryParameters = new HashMap<>();
    private Map<String, String> requestParams = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private final HttpServletRequest request;
    private String body;
    private String host;
    private String pathInfo;

    private RequestStub(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String queryParams(String name) {
        if (queryParameters.get(name) == null) {
            return "";
        }

        return queryParameters.get(name).get(0);
    }

    @Override
    public Set<String> queryParams() {
        return queryParameters.keySet();
    }

    @Override
    public String[] queryParamsValues(String queryParam) {
        List<String> values = queryParameters.get(queryParam);
        if (values == null) {
            values = new LinkedList<>();
        }

        String[] vals = new String[values.size()];

        for (int i = 0; i < values.size(); i++) {
            vals[i] = values.get(i);
        }

        return vals;
    }

    @Override
    public String params(String param) {
        return requestParams.get(param);
    }

    @Override
    public String cookie(String cookieName) {
        return "";
    }

    @Override
    public String pathInfo() {
        return pathInfo;
    }

    @Override
    public Session session() {
        HttpSession httpSession = mock(HttpSession.class);
        return new Session(httpSession, this);
    }

    @Override
    public HttpServletRequest raw() {
        return request;
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public Set<String> headers() {
        return headers.keySet();
    }

    @Override
    public String headers(String header) {
        return headers.get(header);
    }

    public static class RequestBuilder {
        private final Map<String, String> requestParams = new HashMap<>();
        private final Map<String, String> headers = new HashMap<>();
        private final Map<String, List<String>> queryParams = new HashMap<>();
        private Map<String, Part> parts;
        private String body;
        private String host;
        private String pathInfo;

        public RequestBuilder() {
        }

        public RequestBuilder withQueryParam(String param, String value) {
            if (!queryParams.containsKey(param)) {
                queryParams.put(param, new LinkedList<>());
            }
            List<String> values = queryParams.get(param);
            values.add(value);

            return this;
        }

        public RequestBuilder withHeader(String header, String headerValue) {
            this.headers.put(header, headerValue);
            return this;
        }

        public RequestBuilder withMultiPart(String name, Part part) {
            if (parts == null) {
                parts = new HashMap<>();
            }

            parts.put(name, part);

            return this;
        }

        public RequestBuilder withPathParameter(String param, String value) {
            requestParams.put(param, value);
            return this;
        }

        public RequestBuilder withBodyAsJson(String json) {
            this.body = json;
            return this;
        }

        public RequestBuilder withBody(Object body) {
            Serializer jsonTransformer = new Serializer();
            this.body = jsonTransformer.serialize(body);
            return this;
        }

        public RequestBuilder withPayload(Object payload) {
            return withBody(payload);
        }

        public RequestBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public RequestBuilder withPathInfo(String pathInfo) {
            this.pathInfo = pathInfo;
            return this;
        }

        public Request build() {
            HttpServletRequestStub httpServletRequest = new HttpServletRequestStub();
            RequestStub request = new RequestStub(httpServletRequest);

            if (!requestParams.isEmpty()) {
                request.requestParams = requestParams;
            }

            request.queryParameters = queryParams;

            if (parts != null) {
                httpServletRequest.setParts(parts);
            }

            request.headers = headers;

            request.body = body;
            request.host = host;
            request.pathInfo = pathInfo;

            return request;
        }
    }
}