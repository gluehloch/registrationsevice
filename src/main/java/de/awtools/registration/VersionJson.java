package de.awtools.registration;

/**
 * Returns a version string.
 * 
 * @author winkler
 */
public class VersionJson {

    private static final String VERSION = "V0.0.0.1-dev";
    private static final VersionJson VERSION_JSON = new VersionJson();

    private VersionJson() {
    }

    public static VersionJson of() {
        return VERSION_JSON;
    }

    public final String getVersion() {
        return VERSION;
    }

    @Override
    public String toString() {
        return VERSION;
    }

}
