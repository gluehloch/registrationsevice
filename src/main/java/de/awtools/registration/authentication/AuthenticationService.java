package de.awtools.registration.authentication;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.awtools.registration.password.PasswordEncoderWrapper;
import de.awtools.registration.time.TimeService;
import de.awtools.registration.user.Password;
import de.awtools.registration.user.PrivilegeEntity;
import de.awtools.registration.user.PrivilegeRepository;
import de.awtools.registration.user.UserAccountEntity;
import de.awtools.registration.user.UserAccountRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthenticationService implements UserDetailsService {

	/** Should this be configured? */
	private static final long EXPIRATION_DAYS = 3;

	/** Could the be a better issuer? */
	private static final String ISSUER = "awregister";

	/** Should this be configured? */
	private static final ZoneId DEFAULT_TIME_ZONE_ID = ZoneId.systemDefault();

	private static final KeyPair KEY_PAIR;

	static {
		// TODO Auslagerung in eine Datei?!
		KEY_PAIR = Keys.keyPairFor(SignatureAlgorithm.RS256);
	}

	private final AuthenticationRepository authenticationRepository;
	private final UserAccountRepository userAccountRepository;
	private final PrivilegeRepository privilegeRepository;
	private final TimeService timeService;
	private final PasswordEncoderWrapper passwordEncoderWrapper;

	@Autowired
	public AuthenticationService(AuthenticationRepository authenticationRepository,
			UserAccountRepository userAccountRepository, PrivilegeRepository privilegeRepository,
			TimeService timeService, PasswordEncoderWrapper passwordEncoderWrapper) {

		this.authenticationRepository = authenticationRepository;
		this.userAccountRepository = userAccountRepository;
		this.privilegeRepository = privilegeRepository;
		this.timeService = timeService;
		this.passwordEncoderWrapper = passwordEncoderWrapper;
	}

	public Optional<Token> login(String nickname, Password decodedPassword) {
		Optional<UserAccountEntity> user = userAccountRepository.findByNickname(nickname)
		        .filter(p -> passwordEncoderWrapper.validate(decodedPassword, p.getPassword()));

		return user.map(UserAccountEntity::getNickname).map(this::token);

		// Token token = Result.attempt(() -> loadUserByUsername(nickname).getUsername()).map(this::token).orElseThrow();
		// return Optional.ofNullable(token);
	}

	public void logout(Token token) {
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final UserAccountEntity user = userAccountRepository.findByNickname(username)
				.orElseThrow(() -> new UsernameNotFoundException("Unknown user with nickname=[" + username + "]."));

		AWUserDetails.AWUserDetailsBuilder userDetailsBuilder = AWUserDetails.AWUserDetailsBuilder
				.of(user.getNickname(), user.getPassword().get());

		Set<PrivilegeEntity> privileges = privilegeRepository.findByNickname(user.getNickname());

		for (PrivilegeEntity privilege : privileges) {
			userDetailsBuilder.addGrantedAuthority(new SimpleGrantedAuthority(privilege.getName()));
		}

		return userDetailsBuilder.build();
	}

	Token token(String nickname) {
		LocalDateTime tokenExpiration = timeService.now().plusDays(EXPIRATION_DAYS);

		String jws = Jwts.builder().setSubject(nickname).setIssuer(ISSUER)
				.setIssuedAt(timeService.currently())
				.setExpiration(TimeService.convertToDateViaInstant(DEFAULT_TIME_ZONE_ID, tokenExpiration))
				.signWith(KEY_PAIR.getPrivate()).compact();

		return new Token(jws);
	}

}
