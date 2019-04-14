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

    @NotNull
    private String remoteaddress;

    @NotNull
    private String browser;

    private boolean acceptCookies;

    public String getRemoteaddress() {
        return remoteaddress;
    }

    public void setRemoteaddress(String remoteaddress) {
        this.remoteaddress = remoteaddress;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public boolean isAcceptCookies() {
        return acceptCookies;
    }

    public void setAcceptCookies(boolean acceptCookies) {
        this.acceptCookies = acceptCookies;
    }

}
