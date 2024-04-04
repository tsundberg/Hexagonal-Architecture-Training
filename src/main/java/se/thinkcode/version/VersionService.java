package se.thinkcode.version;

public class VersionService {

    private final VersionRepository repository;

    public VersionService(VersionRepository repository) {
        this.repository = repository;
    }

    public void addVersion(Version version) {
        repository.addVersion(version);
    }

    public Version getVersion() {
        return repository.getVersion();
    }
}
