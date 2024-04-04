package se.thinkcode.version;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VersionServiceTest {
    private final VersionRepository repository = new InMemoryVersionRepository();
    private final VersionService service = new VersionService(repository);

    @Test
    void should_get_version() {
        Version expected = new Version("1");
        service.addVersion(expected);

        Version actual = service.getVersion();

        assertThat(actual).isEqualTo(expected);
    }
}