package de.munichdeveloper.user.controller;

import de.munichdeveloper.user.dto.*;
import de.munichdeveloper.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) throws Exception {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) throws Exception {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @PostMapping("/signinByMagicToken")
    public ResponseEntity<JwtAuthenticationResponse> signinByMagicToken(@RequestBody SigninWithDIDRequest request) throws IOException, InterruptedException {
        return ResponseEntity.ok(authenticationService.signinByMagicLink(request.getDid()));
    }
}
