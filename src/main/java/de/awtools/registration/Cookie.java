package de.awtools.registration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Stores the user acceptance to store cookies in the browser.
 *
 * @author Andre Winkler
 */
@Entity
@Table(name = "cookie")
public class Cookie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "website")
    @Size(max = 50)
    private String website;
    
    @Column(name = "remoteaddress")
    @Size(max = 100)
    private String remoteaddress;

    @Column(name = "browser")
    @Size(max = 200)
    @NotNull
    private String browser;

    @Column(name = "created")
    @NotNull
    private LocalDateTime created;

    @Column(name = "acceptcookie")
    private boolean acceptCookie;

    public Long getId() {
        return id;
    }

    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getRemoteaddress() {
        return this.remoteaddress;
    }

    public void setRemoteaddress(String remoteaddress) {
        this.remoteaddress = remoteaddress;
    }

    public String getBrowser() {
        return this.browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public LocalDateTime getCreated() {
        return this.created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isAcceptingCookie() {
        return acceptCookie;
    }

    public void setAcceptCookie(boolean acceptCookie) {
        this.acceptCookie = acceptCookie;
    }

}
