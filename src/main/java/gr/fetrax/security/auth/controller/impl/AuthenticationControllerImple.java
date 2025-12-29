package gr.fetrax.security.auth.controller.impl;

import bank.money.transfer.security.auth.*;
import gr.fetrax.security.auth.AuthenticationRequest;
import gr.fetrax.security.auth.AuthenticationResponse;
import gr.fetrax.security.auth.AuthenticationService;
import gr.fetrax.security.auth.RegisterRequest;
import gr.fetrax.security.auth.controller.AuthenticationController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImple implements AuthenticationController {

    private final AuthenticationService authenticationService;

   @Override
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));

    }
}
