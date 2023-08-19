package de.munichdeveloper.user.service;


import de.munichdeveloper.user.domain.User;
import de.munichdeveloper.user.dto.JwtAuthenticationResponse;
import de.munichdeveloper.user.dto.SignUpRequest;
import de.munichdeveloper.user.dto.SigninRequest;
import de.munichdeveloper.user.enums.Role;
import de.munichdeveloper.user.magic.DIDToken;
import de.munichdeveloper.user.magic.DIDTokenService;
import de.munichdeveloper.user.magic.DIDUserService;
import de.munichdeveloper.user.magic.UserMetadata;
import de.munichdeveloper.user.repository.MagicLinkRepository;
import de.munichdeveloper.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private MagicLinkRepository magicLinkRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private DIDUserService didUserService;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) throws Exception {
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

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException authenticationException) {
            throw new Exception("auth not succeeded");
        }
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user.getEmail());
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signinByMagicLink(String didToken) throws IOException, InterruptedException {
        DIDToken parsedDIDToken = DIDTokenService.parseAndValidateToken(didToken);
        String issuer = parsedDIDToken.getIssuer();
        UserMetadata metadataByIssuer = didUserService.getMetadataByIssuer(issuer);
        String jwt = jwtService.generateToken(metadataByIssuer.getData().getEmail());
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

}
