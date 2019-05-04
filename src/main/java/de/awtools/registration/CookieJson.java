package de.awtools.registration;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * A JSON wrapper for the cookie confirmation of a user.
 *
 * @author winkler
 */
public class CookieJson implements Serializable {

    private static final long serialVersionUID = 6847203448959868549L;

    private boolean acceptCookies;

    public boolean isAcceptCookies() {
        return acceptCookies;
    }

    public void setAcceptCookies(boolean acceptCookies) {
        this.acceptCookies = acceptCookies;
    }

}
