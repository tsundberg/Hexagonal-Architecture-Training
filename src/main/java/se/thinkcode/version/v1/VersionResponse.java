package se.thinkcode.version.v1;

import se.thinkcode.version.Version;

public record VersionResponse(String version) {

    public static VersionResponse fromModel(Version version) {
        String versionValue = "0";
        if (version != null) {
            versionValue = version.version();
        }

        return new VersionResponse(versionValue);
    }
}
