package de.munichdeveloper.magic.controller;

import de.munichdeveloper.magic.dto.SigninWithDIDRequest;
import de.munichdeveloper.magic.service.MagicService;
import de.munichdeveloper.user.dto.JwtAuthenticationResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/magic")
@AllArgsConstructor
public class MagicController {

    @Autowired
    private MagicService magicService;

    @PostMapping("/signinByMagicToken")
    public ResponseEntity<JwtAuthenticationResponse> signinByMagicToken(@RequestBody SigninWithDIDRequest request) throws IOException, InterruptedException {
        return ResponseEntity.ok(magicService.signinByMagicLink(request.getDid()));
    }
}
