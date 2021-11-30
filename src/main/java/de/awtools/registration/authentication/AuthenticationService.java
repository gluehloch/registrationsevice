package de.awtools.registration.authentication;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

	@Autowired
	public AuthenticationService(AuthenticationRepository authenticationRepository,
			UserAccountRepository userAccountRepository, PrivilegeRepository privilegeRepository,
			TimeService timeService) {

		this.authenticationRepository = authenticationRepository;
		this.userAccountRepository = userAccountRepository;
		this.privilegeRepository = privilegeRepository;
		this.timeService = timeService;
	}

	public Optional<Token> login(String nickname, Password password) {
		UserAccountEntity user = userAccountRepository.findByNickname(nickname)
				.orElseThrow(() -> new EntityNotFoundException("Unknown user with nickname=[" + nickname + "]."));

		if (Password.isEqual(user.getPassword(), password)) {
			return Optional.empty();
		}

		Token token = Result.attempt(() -> loadUserByUsername(nickname)).map(this::token).orElseThrow();
		return Optional.ofNullable(token);
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

	Token token(UserDetails user) {
		LocalDateTime tokenExpiration = timeService.now().plusDays(EXPIRATION_DAYS);

		String jws = Jwts.builder().setSubject(user.getUsername()).setIssuer(ISSUER)
				.setIssuedAt(timeService.currently())
				.setExpiration(TimeService.convertToDateViaInstant(DEFAULT_TIME_ZONE_ID, tokenExpiration))
				.signWith(KEY_PAIR.getPrivate()).compact();

		return new Token(jws);
	}

	@FunctionalInterface
	public interface CheckedSupplier<V, E extends Throwable> {
		V get() throws E;

		public static <V, E extends Throwable> Result<V, E> attempt(CheckedSupplier<? extends V, ? extends E> p) {
			try {
				return Result.success(p.get());
			} catch (Throwable e) {
				@SuppressWarnings("unchecked")
				E err = (E) e;
				return Result.failure(err);
			}
		}
	}

	public static class Result<V, E extends Throwable> {

		private final V value;
		private final E error;

		private Result(V value, E error) {
			this.value = value;
			this.error = error;
		}

		public static <V, E extends Throwable> Result<V, E> failure(E error) {
			return new Result<>(null, Objects.requireNonNull(error));
		}

		public static <V, E extends Throwable> Result<V, E> success(V value) {
			return new Result<>(Objects.requireNonNull(value), null);
		}

	    public static <V, E extends Throwable> Result<V, E> attempt(CheckedSupplier<? extends V, ? extends E> p) {
	        try {
	            return Result.success(p.get());
	        } catch (Throwable e) {
	            @SuppressWarnings("unchecked")
	            E err = (E) e;
	            return Result.failure(err);
	        }
	    }

		// ----

		public <T> Result<T, E> map(Function<? super V, ? extends T> mapper) {
			return Optional.ofNullable(error).map(e -> Result.<T, E>failure(e))
					.orElseGet(() -> Result.success(mapper.apply(value)));
		}

		public V orElseThrow() throws E {
			return Optional.ofNullable(value).orElseThrow(() -> error);
		}

	}

}
