package de.awtools.registration;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Autowired
    private TimeService timeService;

    @Autowired
    private CookieRepository cookieRepository;

    @Transactional
    public Cookie storeCookieAcceptance(String website, String browser,
            String remoteaddress, boolean acceptCookies) {

        Cookie cookie = new Cookie();
        cookie.setWebsite(website);
        cookie.setAcceptCookie(acceptCookies);
        cookie.setBrowser(browser);
        cookie.setRemoteaddress(remoteaddress);
        cookie.setCreated(timeService.now());
        cookie = cookieRepository.save(cookie);
        return cookie;
    }

}
