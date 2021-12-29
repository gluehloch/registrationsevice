package de.awtools.registration;

/**
 * Returns a version string.
 * 
 * @author winkler
 */
public class VersionJson {

    private final String version;

    private VersionJson(String version) {
        this.version = version;
    }

    public static VersionJson of(String groupId, String artifactId, String version) {
        return new VersionJson(String.format("%s-%s-%s", groupId, artifactId, version));
    }

    public final String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return version;
    }

}
