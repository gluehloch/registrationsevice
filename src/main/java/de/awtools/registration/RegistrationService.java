package de.awtools.registration;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.awtools.registration.RegistrationValidation.ValidationCode;
import de.awtools.registration.password.PasswordEncoderWrapper;

/**
 * Register and confirm a new user.
 * 
 * @author winkler
 */
@Service
public class RegistrationService {

    private static final Logger LOG = LoggerFactory
            .getLogger(RegistrationService.class);

    @Autowired
    private PasswordEncoderWrapper passwordEncoder;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private TimeService timeService;

    /**
     * Starts the registration process.
     * 
     * @param nickname
     *            nickname
     * @param email
     *            the email address
     * @param password
     *            password
     * @param name
     *            real name
     * @param firstname
     *            real firstname
     * @param application
     *            the application to register for
     * @param acceptMail
     *            user accepts mails
     * @param acceptCookie
     *            user accepts cookies
     * @param supplement
     *            untyped supplement data
     * @return RegistrationValidation
     * @throws RequestValidationException
     *             Request validation exception
     */
    @Transactional
    public RegistrationValidation registerNewUserAccount(String nickname,
            String email, String password, String name, String firstname,
            String application, boolean acceptMail, boolean acceptCookie,
            String supplement)
            throws RequestValidationException {

        if (!acceptMail || !acceptCookie) {
            return new RegistrationValidation(nickname,
                    ValidationCode.ILLEGAL_ARGUMENTS);
        }

        validateApplication(application);

        Registration registrationDefined = registrationRepository
                .findByNickname(nickname);
        if (registrationDefined != null) {
            LOG.info("Nickname already defined: [%s]", nickname);
            return new RegistrationValidation(nickname,
                    ValidationCode.ILLEGAL_ARGUMENTS);
        }

        LocalDateTime now = timeService.now();

        Registration registration = new Registration();
        registration.setNickname(nickname);
        registration.setFirstname(firstname);
        registration.setName(name);
        registration
                .setPassword(new Password(passwordEncoder.encode(password)));
        registration.setEmail(new Email(email));
        registration.setCreated(now);
        UUID token = UUID.randomUUID();
        registration.setToken(new Token(token.toString()));
        registration.setApplication(application);
        registration.setConfirmed(false);
        registration.setAcceptCookie(acceptCookie);
        registration.setAcceptMail(acceptMail);
        registration.setSupplement(supplement);

        registrationRepository.save(registration);

        return new RegistrationValidation(nickname, ValidationCode.OK);
    }

    @Transactional
    public RegistrationValidation restartUserAccount(String nickname,
            String email, String password, String name, String firstname,
            String application, boolean acceptMail, boolean acceptCookie,
            String supplement)
            throws RequestValidationException {

        registrationRepository.deleteByEmail(new Email(email));
        return registerNewUserAccount(nickname, email, password, name,
                firstname, application, acceptMail, acceptCookie, supplement);
    }

    @Transactional
    public RegistrationValidation confirmAccount(Token token)
            throws RequestValidationException {

        Registration registration = registrationRepository.findByToken(token);
        if (registration == null) {
            return new RegistrationValidation("unkown",
                    ValidationCode.ILLEGAL_ARGUMENTS);
        }

        registration.setConfirmed(true);

        Application application = validateApplication(
                registration.getApplication());

        UserAccount newUserAccount = new UserAccount(timeService.now(),
                registration);
        application.addUser(newUserAccount);

        userAccountRepository.save(newUserAccount);

        return new RegistrationValidation(registration.getNickname(),
                ValidationCode.OK);
    }

    @Transactional
    public RegistrationValidation validate(String nickname, String email,
            String applicationName) throws RequestValidationException {

        validateApplication(applicationName);

        Registration registrationDefined = null;

        registrationDefined = registrationRepository.findByNickname(nickname);
        if (registrationDefined != null) {
            return new RegistrationValidation(nickname,
                    ValidationCode.KNOWN_NICKNAME);
        }

        registrationDefined = registrationRepository
                .findByEmail(new Email(email));
        if (registrationDefined != null) {
            return new RegistrationValidation(nickname,
                    ValidationCode.KNOWN_MAILADDRESS);
        }

        return new RegistrationValidation(nickname, ValidationCode.OK);
    }

    private Application validateApplication(String applicationName)
            throws RequestValidationException {

        Application application = applicationRepository
                .findByName(applicationName);

        if (application == null) {
            throw new RequestValidationException(
                    new RegistrationValidation("unknown",
                            ValidationCode.UNKNOWN_APPLICATION));
        }

        return application;
    }

}
