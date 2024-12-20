package es.localchat.component.auth.controller;

import es.local.chat.sharedentities.exception.ClientUUIDNotFoundException;

import es.localchat.component.auth.model.JWTResponse;
import es.localchat.component.auth.model.SignUpRequest;
import es.localchat.component.auth.model.SigninUpResponse;
import es.localchat.component.auth.service.AuthenticationService;
import es.localchat.component.auth.service.impl.JwtServiceLogic;
import es.localchat.component.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtServiceLogic jwtServiceLogic;
    private final AuthenticationService authenticationService;

    @GetMapping("/signin")
    public ResponseEntity<JWTResponse> authenticate(
            @RequestParam String uuid
    ) {
        var user = userRepository.findByUUID(uuid);
        if (!user.isEmpty()) {
            return ResponseEntity.ok(
            ).body(
                    JWTResponse.builder()
                            .JWT("Bearer " + jwtServiceLogic.generateToken(uuid))
                            .build()
            );
        } else {
            throw new ClientUUIDNotFoundException("Client UUID not found");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<SigninUpResponse> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

}
