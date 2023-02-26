package de.awtools.registration.register;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import de.awtools.registration.mail.SendMailService;
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

    @Autowired
    private SendMailService sendMailService;

    /**
     * Starts the registration process.
     * 
     * @param registration RegistrationJson

     * @return RegistrationValidation
     * @throws RequestValidationException Request validation exception
     */
    @Transactional
    public DefaultRegistrationValidation registerNewAccount(RegistrationJson registration)
            throws RequestValidationException {

        DefaultRegistrationValidation registrationValidation = new DefaultRegistrationValidation(
                registration.getNickname(), registration.getApplicationName());

        if (!registration.isAcceptCookie()) {
            registrationValidation.addValidationCode(Validation.ValidationCode.MISSING_ACCEPT_COOKIE);
        }

        if (!registration.isAcceptMail()) {
            registrationValidation.addValidationCode(Validation.ValidationCode.MISSING_ACCEPT_EMAIL);
        }

        if (StringUtils.isBlank(registration.getPassword()) || registration.getPassword().length() < 5) {
            registrationValidation.addValidationCode(Validation.ValidationCode.PASSWORD_TOO_SHORT);
        }
        
        if (StringUtils.isBlank(registration.getNickname())) {
            registrationValidation.addValidationCode(Validation.ValidationCode.NICKNAME_IS_EMPTY);
        }
        
        if (StringUtils.isBlank(registration.getFirstname())) {
            registrationValidation.addValidationCode(Validation.ValidationCode.FIRSTNAME_IS_EMPTY);
        }

        if (StringUtils.equalsAnyIgnoreCase(registration.getNickname(), registration.getPassword())) {
            registrationValidation.addValidationCode(Validation.ValidationCode.PASSWORD_IS_TOO_SIMPEL);
        }

        if (StringUtils.isBlank(registration.getEmail())) {
            registrationValidation.addValidationCode(Validation.ValidationCode.EMAIL_IS_EMPTY);
        } else {
            if (!EmailValidator.getInstance().isValid(registration.getEmail())) {
                registrationValidation.addValidationCode(Validation.ValidationCode.EMAIL_IS_NOT_VALID);
            }
            
            if (registrationRepository.findByEmail(Email.of(registration.getEmail())).isPresent()) {
                registrationValidation.addValidationCode(Validation.ValidationCode.EMAIL_IS_RESERVED);
            }     
        }
        
        Optional<ApplicationEntity> application = applicationRepository.findByName(registration.getApplicationName());
        if (application.isEmpty()) {
            registrationValidation.addValidationCode(Validation.ValidationCode.UNKNOWN_APPLICATION);
        }

        Optional<UserAccountEntity> userAccountCheck = userAccountRepository.findByNickname(registration.getNickname());
        if (userAccountCheck.isPresent()) {
            registrationValidation.addValidationCode(Validation.ValidationCode.KNOWN_NICKNAME);
        }

        Optional<RegistrationEntity> registrationCheck = registrationRepository.findByNickname(registration.getNickname());
        if (registrationCheck.isPresent()) {
            registrationValidation.addValidationCode(Validation.ValidationCode.KNOWN_NICKNAME);
        }

        if (registrationValidation.ok()) {
            RegistrationEntity registrationEntity = createRegistration(registration);
            registrationRepository.save(registrationEntity);

            registrationValidation.addValidationCode(Validation.ValidationCode.OK);
        }

        return registrationValidation;
    }

    private RegistrationEntity createRegistration(RegistrationJson registration) {
        LocalDateTime now = timeService.now();

        RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setNickname(registration.getNickname());
        registrationEntity.setFirstname(registration.getFirstname());
        registrationEntity.setName(registration.getName());
        registrationEntity.setPassword(passwordEncoder.encode(Password.decoded(registration.getPassword())));
        registrationEntity.setEmail(Email.of(registration.getEmail()));
        registrationEntity.setCreated(now);
        UUID token = UUID.randomUUID();
        registrationEntity.setToken(new Token(token.toString()));
        registrationEntity.setApplication(registration.getApplicationName());
        registrationEntity.setConfirmed(false);
        registrationEntity.setAcceptCookie(registration.isAcceptCookie());
        registrationEntity.setAcceptMail(registration.isAcceptMail());
        registrationEntity.setSupplement(registration.getSupplement());

        /* TODO
        sendMailService.sendMail(
                "do-not-replay@tippdiekistebier.de",
                registration.getEmail().toString(),
                "Registrierung bestätigen.",
                "Confirm link..."
                );
         */

        return registrationEntity;
    }

    @Transactional
    public DefaultRegistrationValidation restartAccount(RegistrationJson registration)
            throws RequestValidationException {

        registrationRepository.deleteByEmail(Email.of(registration.getEmail()));
        return registerNewAccount(registration);
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
     * @param registration Registration
     *
     * @return RegistrationValidation
     * @throws RequestValidationException
     *             Request validation exception
     */
    @Transactional
    public DefaultRegistrationValidation createAccount(RegistrationJson registration) {
        final DefaultRegistrationValidation registrationValidation = registerNewAccount(registration);
        if (registrationValidation.isNotOk()) {
            return registrationValidation;
        }

        final UserAccountEntity userAccount = registrationRepository.findByNickname(registration.getNickname())
                .map(this::confirmAccount)
                .map(this::createUserAccountEntity)
                .orElseThrow();

        applicationRepository.findByName(registration.getApplicationName())
                .map(app -> this.addUserAccountToApplication(app, userAccount));

        return new DefaultRegistrationValidation(
                registration.getNickname(), registration.getApplicationName(), Validation.ValidationCode.OK);
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
