package de.munichdeveloper.magic.controller;

import de.munichdeveloper.magic.dto.SigninWithDIDRequest;
import de.munichdeveloper.magic.service.MagicService;
import de.munichdeveloper.user.dto.JwtAuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MagicControllerTest {

    private MagicController magicController;
    private MagicService magicService;

    @BeforeEach
    void setUp() {
        magicService = mock(MagicService.class);
        magicController = new MagicController(magicService);
    }

    @Test
    void testSigninByMagicToken() throws Exception {
        SigninWithDIDRequest signinWithDIDRequest = new SigninWithDIDRequest("did");
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("token");

        when(magicService.signinByMagicLink(signinWithDIDRequest.getDid())).thenReturn(response);

        ResponseEntity<JwtAuthenticationResponse> result = magicController.signinByMagicToken(signinWithDIDRequest);

        verify(magicService).signinByMagicLink(signinWithDIDRequest.getDid());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}