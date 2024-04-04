package se.thinkcode.version.v1;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import se.thinkcode.infrastructure.Deserializer;
import se.thinkcode.version.InMemoryVersionRepository;
import se.thinkcode.version.Version;
import se.thinkcode.version.VersionRepository;
import se.thinkcode.version.VersionService;
import spark.Request;
import spark.RequestStub;
import spark.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class VersionControllerTest {
    private final Deserializer deserializer = new Deserializer();
    private final VersionRepository repository = new InMemoryVersionRepository();
    private final VersionService service = new VersionService(repository);
    private final VersionController controller = new VersionController(service);

    @Test
    void should_get_version() {
        Version version = new Version("2");
        service.addVersion(version);
        VersionResponse expected = new VersionResponse("2");
        Request request = new RequestStub.RequestBuilder()
                .build();
        Response response = mock(Response.class);

        String json = controller.handle(request, response);
        VersionResponse actual = deserializer.fromJson(json, VersionResponse.class);

        assertThat(actual).isEqualTo(expected);
        verify(response).type("application/json");
        verify(response).status(200);
    }
}
