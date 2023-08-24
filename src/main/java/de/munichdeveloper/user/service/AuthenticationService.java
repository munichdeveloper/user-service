package de.munichdeveloper.user.service;

import de.munichdeveloper.user.domain.User;
import de.munichdeveloper.user.dto.JwtAuthenticationResponse;
import de.munichdeveloper.user.dto.SignUpRequest;
import de.munichdeveloper.user.dto.SigninRequest;
import de.munichdeveloper.user.enums.Role;
import de.munichdeveloper.user.exception.FeatureNotEnabledException;
import de.munichdeveloper.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${enable.signin.withUserAndPassword}")
    private boolean enableSigninWithUserAndPassword;

    @Value("${enable.signup.withUserAndPassword}")
    private boolean enableSignupWithUserAndPassword;

    public JwtAuthenticationResponse signup(SignUpRequest request) throws Exception {
        if (!enableSignupWithUserAndPassword) {
            throw new FeatureNotEnabledException("This feature is currently disabled. You can set 'enable.signup.withUserAndPassword' to 'true' to enable it.");
        }
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            // user already exists!
            throw new Exception("User with email " + request.getEmail() + " already registered");
        }
        var user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user.getEmail());
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public JwtAuthenticationResponse signin(SigninRequest request) throws Exception {
        if (!enableSigninWithUserAndPassword) {
            throw new FeatureNotEnabledException("This feature is currently disabled. You can set 'enable.signin.withUserAndPassword' to 'true' to enable it.");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException authenticationException) {
            throw new Exception("auth not succeeded");
        }
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user.getEmail());
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

}
