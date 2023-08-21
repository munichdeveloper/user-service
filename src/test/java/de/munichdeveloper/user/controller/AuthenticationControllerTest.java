package de.munichdeveloper.user.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import de.munichdeveloper.user.dto.JwtAuthenticationResponse;
import de.munichdeveloper.user.dto.SignUpRequest;
import de.munichdeveloper.user.dto.SigninRequest;
import de.munichdeveloper.user.dto.SigninWithDIDRequest;
import de.munichdeveloper.user.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthenticationControllerTest {

    private AuthenticationController authenticationController;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = mock(AuthenticationService.class);
        authenticationController = new AuthenticationController(authenticationService);
    }

    @Test
    void testSignup() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("firstname", "lastname", "username", "password");
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("token");

        when(authenticationService.signup(signUpRequest)).thenReturn(response);

        ResponseEntity<JwtAuthenticationResponse> result = authenticationController.signup(signUpRequest);

        verify(authenticationService).signup(signUpRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testSignin() throws Exception {
        SigninRequest signinRequest = new SigninRequest("username", "password");
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("token");

        when(authenticationService.signin(signinRequest)).thenReturn(response);

        ResponseEntity<JwtAuthenticationResponse> result = authenticationController.signin(signinRequest);

        verify(authenticationService).signin(signinRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testSigninByMagicToken() throws Exception {
        SigninWithDIDRequest signinWithDIDRequest = new SigninWithDIDRequest("did");
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("token");

        when(authenticationService.signinByMagicLink(signinWithDIDRequest.getDid())).thenReturn(response);

        ResponseEntity<JwtAuthenticationResponse> result = authenticationController.signinByMagicToken(signinWithDIDRequest);

        verify(authenticationService).signinByMagicLink(signinWithDIDRequest.getDid());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}
