package de.awtools.registration.register;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.awtools.registration.RequestValidationException;
import de.awtools.registration.Token;
import de.awtools.registration.password.PasswordEncoderWrapper;
import de.awtools.registration.time.TimeService;
import de.awtools.registration.user.ApplicationEntity;
import de.awtools.registration.user.ApplicationRepository;
import de.awtools.registration.user.Email;
import de.awtools.registration.user.Password;
import de.awtools.registration.user.UserAccountEntity;
import de.awtools.registration.user.UserAccountRepository;

/**
 * Register and confirm a new user.
 * 
 * @author winkler
 */
@Service
public class RegistrationService {

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
    public DefaultRegistrationValidation registerNewAccount(String nickname,
                                                            String email,
                                                            String password,
                                                            String name,
                                                            String firstname,
                                                            String applicationName,
                                                            boolean acceptMail,
                                                            boolean acceptCookie,
                                                            String supplement)
            throws RequestValidationException {

        DefaultRegistrationValidation registrationValidation = new DefaultRegistrationValidation(nickname, applicationName);

        if (!acceptCookie) {
            registrationValidation.addValidationCode(Validation.ValidationCode.MISSING_ACCEPT_COOKIE);
        }

        if (!acceptMail) {
            registrationValidation.addValidationCode(Validation.ValidationCode.MISSING_ACCEPT_EMAIL);
        }

        if (StringUtils.isBlank(password) || password.length() < 5) {
            registrationValidation.addValidationCode(Validation.ValidationCode.PASSWORD_TOO_SHORT);
        }
        
        if (StringUtils.isBlank(nickname)) {
            registrationValidation.addValidationCode(Validation.ValidationCode.NICKNAME_IS_EMPTY);
        }
        
        if (StringUtils.isBlank(firstname)) {
            registrationValidation.addValidationCode(Validation.ValidationCode.FIRSTNAME_IS_EMPTY);
        }

        if (StringUtils.equalsAnyIgnoreCase(nickname, password)) {
            registrationValidation.addValidationCode(Validation.ValidationCode.PASSWORD_IS_TOO_SIMPEL);
        }

        if (StringUtils.isBlank(email)) {
            registrationValidation.addValidationCode(Validation.ValidationCode.EMAIL_IS_EMPTY);
        } else {
            if (!EmailValidator.getInstance().isValid(email)) {
                registrationValidation.addValidationCode(Validation.ValidationCode.EMAIL_IS_NOT_VALID);
            }
            
            if (registrationRepository.findByEmail(Email.of(email)).isPresent()) {
                registrationValidation.addValidationCode(Validation.ValidationCode.EMAIL_IS_RESERVED);
            }     
        }
        
        Optional<ApplicationEntity> application = applicationRepository.findByName(applicationName);
        if (application.isEmpty()) {
            registrationValidation.addValidationCode(Validation.ValidationCode.UNKNOWN_APPLICATION);
        }

        Optional<UserAccountEntity> userAccountCheck = userAccountRepository.findByNickname(nickname);
        if (userAccountCheck.isPresent()) {
            registrationValidation.addValidationCode(Validation.ValidationCode.KNOWN_NICKNAME);
        }

        Optional<RegistrationEntity> registrationCheck = registrationRepository.findByNickname(nickname);
        if (registrationCheck.isPresent()) {
            registrationValidation.addValidationCode(Validation.ValidationCode.KNOWN_NICKNAME);
        }

        if (registrationValidation.ok()) {
            RegistrationEntity registration = createRegistration(nickname, email, password, name, firstname, applicationName, acceptMail, acceptCookie, supplement);
            registrationRepository.save(registration);

            registrationValidation.addValidationCode(Validation.ValidationCode.OK);
        }

        return registrationValidation;
    }

    private RegistrationEntity createRegistration(String nickname,
                                                  String email,
                                                  String password,
                                                  String name,
                                                  String firstname,
                                                  String applicationName,
                                                  boolean acceptMail,
                                                  boolean acceptCookie,
                                                  String supplement) {
        LocalDateTime now = timeService.now();

        RegistrationEntity registration = new RegistrationEntity();
        registration.setNickname(nickname);
        registration.setFirstname(firstname);
        registration.setName(name);
        registration.setPassword(passwordEncoder.encode(Password.decoded(password)));
        registration.setEmail(Email.of(email));
        registration.setCreated(now);
        UUID token = UUID.randomUUID();
        registration.setToken(new Token(token.toString()));
        registration.setApplication(applicationName);
        registration.setConfirmed(false);
        registration.setAcceptCookie(acceptCookie);
        registration.setAcceptMail(acceptMail);
        registration.setSupplement(supplement);

        return registration;
    }

    @Transactional
    public DefaultRegistrationValidation restartAccount(String nickname,
                                                        String email, String password, String name, String firstname,
                                                        String application, boolean acceptMail, boolean acceptCookie,
                                                        String supplement)
            throws RequestValidationException {

        registrationRepository.deleteByEmail(Email.of(email));
        return registerNewAccount(nickname, email, password, name,
                firstname, application, acceptMail, acceptCookie, supplement);
    }

    @Transactional
    public DefaultRegistrationValidation confirmAccount(Token token)
            throws RequestValidationException {

        Optional<RegistrationEntity> optionalRegistration = registrationRepository.findByToken(token);
        if (optionalRegistration.isEmpty()) {
            return new DefaultRegistrationValidation(null, null, Validation.ValidationCode.UNKNOWN_TOKEN);
        }

        RegistrationEntity registration = optionalRegistration.get();

        registration.setConfirmed(true);

        ApplicationEntity application = validateApplication(
                registration.getNickname(),
                registration.getApplication());

        UserAccountEntity newUserAccount = new UserAccountEntity(timeService.now(),
                registration);
        application.addUser(newUserAccount);

        userAccountRepository.save(newUserAccount);

        return new DefaultRegistrationValidation(registration.getNickname(),
                registration.getApplication(),
                Validation.ValidationCode.OK);
    }

    @Transactional
    public DefaultRegistrationValidation validate(String nickname, String email,
                                                  String applicationName) throws RequestValidationException {

        validateApplication(nickname, applicationName);

        Optional<RegistrationEntity> registrationDefined = registrationRepository.findByNickname(nickname);
        if (registrationDefined.isPresent()) {
            return new DefaultRegistrationValidation(nickname, applicationName, Validation.ValidationCode.KNOWN_NICKNAME);
        }

        registrationDefined = registrationRepository.findByEmail(Email.of(email));
        if (registrationDefined.isPresent()) {
            return new DefaultRegistrationValidation(nickname, applicationName, Validation.ValidationCode.KNOWN_MAIL_ADDRESS);
        }

        return new DefaultRegistrationValidation(nickname, applicationName,
                Validation.ValidationCode.OK);
    }

    private ApplicationEntity validateApplication(String nickname, String applicationName) throws RequestValidationException {

        ApplicationEntity application = applicationRepository.findByName(applicationName)
                .orElseThrow(() -> new RequestValidationException(
                        new DefaultRegistrationValidation(nickname, applicationName, Validation.ValidationCode.UNKNOWN_APPLICATION)));

        return application;
    }

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
    public DefaultRegistrationValidation createAccount(String nickname,
                                                       String email,
                                                       String password,
                                                       String name,
                                                       String firstname,
                                                       String applicationName,
                                                       boolean acceptMail,
                                                       boolean acceptCookie,
                                                       String supplement) {
        final DefaultRegistrationValidation registrationValidation = registerNewAccount(nickname, email, password, name, firstname, applicationName, acceptMail, acceptCookie, supplement);
        if (registrationValidation.isNotOk()) {
            return registrationValidation;
        }

        final UserAccountEntity userAccount = registrationRepository.findByNickname(nickname)
                .map(this::confirmAccount)
                .map(this::createUserAccountEntity)
                .orElseThrow();

        applicationRepository.findByName(applicationName)
                .map(app -> this.addUserAccountToApplication(app, userAccount));

        return new DefaultRegistrationValidation(nickname, applicationName, Validation.ValidationCode.OK);
    }

    private RegistrationEntity confirmAccount(RegistrationEntity registration) {
        registration.setConfirmed(true);
        return registration;
    }

    private UserAccountEntity createUserAccountEntity(RegistrationEntity registration) {
        return new UserAccountEntity(timeService.now(), registration);
    }

    private UserAccountEntity addUserAccountToApplication(ApplicationEntity application, UserAccountEntity userAccount) {
        application.addUser(userAccount);
        userAccountRepository.save(userAccount);
        return userAccount;
    }

}
