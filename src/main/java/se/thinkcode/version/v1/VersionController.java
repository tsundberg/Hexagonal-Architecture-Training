package se.thinkcode.version.v1;

import org.eclipse.jetty.http.HttpStatus;
import se.thinkcode.infrastructure.Serializer;
import se.thinkcode.version.Version;
import se.thinkcode.version.VersionService;
import spark.Request;
import spark.Response;

public class VersionController {
    private final Serializer serializer = new Serializer();
    private final VersionService service;

    public VersionController(VersionService service) {
        this.service = service;
    }

    public String handle(Request req, Response res) {
        res.type("application/json");

        Version version = service.getVersion();
        VersionResponse versionResponse = VersionResponse.fromModel(version);
        res.status(HttpStatus.OK_200);

        return serializer.serialize(versionResponse);
    }
}
