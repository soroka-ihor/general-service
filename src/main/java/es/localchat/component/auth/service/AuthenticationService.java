package es.localchat.component.auth.service;


import es.localchat.component.auth.model.JWTResponse;
import es.localchat.component.auth.model.SignInRequest;
import es.localchat.component.auth.model.SignUpRequest;
import es.localchat.component.auth.model.SigninUpResponse;

public interface AuthenticationService {
    SigninUpResponse signUp(SignUpRequest request);
    JWTResponse signIn(SignInRequest request);
}
