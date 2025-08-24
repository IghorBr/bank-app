package ib.api.bank.core.security.authentication;

import ib.api.bank.api.dto.in.AuthIn;
import ib.api.bank.api.dto.out.AuthOut;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<AuthOut> authenticate(@Valid AuthIn authIn) {
        var token = service.authenticate(authIn.accountNumber(), authIn.password());
        return ResponseEntity.ok(new AuthOut(token));
    }

}
