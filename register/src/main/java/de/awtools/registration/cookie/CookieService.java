package de.awtools.registration.cookie;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.awtools.registration.time.TimeService;

@Service
public class CookieService {

    @Autowired
    private TimeService timeService;

    @Autowired
    private CookieRepository cookieRepository;

    @Transactional
    public CookieEntity storeCookieAcceptance(String website, String browser,
            String remoteaddress, boolean acceptCookies) {

        CookieEntity cookie = new CookieEntity();
        cookie.setWebsite(website);
        cookie.setAcceptCookie(acceptCookies);
        cookie.setBrowser(browser);
        cookie.setRemoteaddress(remoteaddress);
        cookie.setCreated(timeService.now());
        cookie = cookieRepository.save(cookie);
        return cookie;
    }

}
