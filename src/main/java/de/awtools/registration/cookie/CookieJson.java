package de.awtools.registration.cookie;

import java.io.Serial;
import java.io.Serializable;

/**
 * A JSON wrapper for the cookie confirmation of a user.
 *
 * @author winkler
 */
public class CookieJson implements Serializable {

    @Serial
    private static final long serialVersionUID = 6847203448959868549L;

    private boolean acceptCookies;
    private String website;

    public boolean isAcceptCookies() {
        return acceptCookies;
    }

    public void setAcceptCookies(boolean acceptCookies) {
        this.acceptCookies = acceptCookies;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}
