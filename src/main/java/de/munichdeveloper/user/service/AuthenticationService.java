package de.munichdeveloper.user.service;


import de.munichdeveloper.user.dto.JwtAuthenticationResponse;
import de.munichdeveloper.user.dto.SignUpRequest;
import de.munichdeveloper.user.dto.SigninRequest;

import java.io.IOException;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request) throws Exception;

    JwtAuthenticationResponse signin(SigninRequest request) throws Exception;
    JwtAuthenticationResponse signinByMagicLink(String didToken) throws IOException, InterruptedException;
}
