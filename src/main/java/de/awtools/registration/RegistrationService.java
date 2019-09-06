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
     * @param applicationName
     *            the application name to register for
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
            String applicationName, boolean acceptMail, boolean acceptCookie,
            String supplement)
            throws RequestValidationException {

        RegistrationValidation registrationValidation = new RegistrationValidation(
                nickname, applicationName);

        if (!acceptCookie) {
            registrationValidation
                    .addValidationCode(ValidationCode.MISSING_ACCEPT_COOKIE);
        }

        if (!acceptMail) {
            registrationValidation
                    .addValidationCode(ValidationCode.MISSING_ACCEPT_EMAIL);
        }

        Application application = applicationRepository
                .findByName(applicationName);
        if (application == null) {
            registrationValidation
                    .addValidationCode(ValidationCode.UNKNOWN_APPLICATION);
        }

        UserAccount userAccountCheck = userAccountRepository
                .findByNickname(nickname);
        if (userAccountCheck != null) {
            registrationValidation
                    .addValidationCode(ValidationCode.KNOWN_NICKNAME);
        }

        Registration registrationCheck = registrationRepository
                .findByNickname(nickname);
        if (registrationCheck != null) {
            registrationValidation
                    .addValidationCode(ValidationCode.KNOWN_NICKNAME);
        }

        if (registrationValidation.ok()) {
            LocalDateTime now = timeService.now();

            Registration registration = new Registration();
            registration.setNickname(nickname);
            registration.setFirstname(firstname);
            registration.setName(name);
            registration
                    .setPassword(
                            new Password(passwordEncoder.encode(password)));
            registration.setEmail(new Email(email));
            registration.setCreated(now);
            UUID token = UUID.randomUUID();
            registration.setToken(new Token(token.toString()));
            registration.setApplication(applicationName);
            registration.setConfirmed(false);
            registration.setAcceptCookie(acceptCookie);
            registration.setAcceptMail(acceptMail);
            registration.setSupplement(supplement);

            registrationRepository.save(registration);

            registrationValidation.addValidationCode(ValidationCode.OK);
        }

        return registrationValidation;
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
            return new RegistrationValidation(null, null,
                    ValidationCode.UNKNOWN_TOKEN);
        }

        registration.setConfirmed(true);

        Application application = validateApplication(
                registration.getNickname(),
                registration.getApplication());

        UserAccount newUserAccount = new UserAccount(timeService.now(),
                registration);
        application.addUser(newUserAccount);

        userAccountRepository.save(newUserAccount);

        return new RegistrationValidation(registration.getNickname(),
                registration.getApplication(),
                ValidationCode.OK);
    }

    @Transactional
    public RegistrationValidation validate(String nickname, String email,
            String applicationName) throws RequestValidationException {

        validateApplication(nickname, applicationName);

        Registration registrationDefined = null;

        registrationDefined = registrationRepository.findByNickname(nickname);
        if (registrationDefined != null) {
            return new RegistrationValidation(nickname,
                    applicationName,
                    ValidationCode.KNOWN_NICKNAME);
        }

        registrationDefined = registrationRepository
                .findByEmail(new Email(email));
        if (registrationDefined != null) {
            return new RegistrationValidation(nickname,
                    applicationName,
                    ValidationCode.KNOWN_MAILADDRESS);
        }

        return new RegistrationValidation(nickname, applicationName,
                ValidationCode.OK);
    }

    private Application validateApplication(String nickname,
            String applicationName)
            throws RequestValidationException {

        Application application = applicationRepository
                .findByName(applicationName);

        if (application == null) {
            throw new RequestValidationException(
                    new RegistrationValidation(nickname, applicationName,
                            ValidationCode.UNKNOWN_APPLICATION));
        }

        return application;
    }

}
