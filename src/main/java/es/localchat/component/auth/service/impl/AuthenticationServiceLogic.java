package es.localchat.component.auth.service.impl;


import es.localchat.component.auth.model.JWTResponse;
import es.localchat.component.auth.model.SignInRequest;
import es.localchat.component.auth.model.SignUpRequest;
import es.localchat.component.auth.model.SigninUpResponse;
import es.localchat.component.auth.service.AuthenticationService;
import es.localchat.component.user.appsettings.service.impl.AppSettingServiceLogic;
import es.localchat.component.user.mapper.UserToUserProfileMapper;
import es.localchat.component.user.service.impl.UserServiceLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceLogic implements AuthenticationService {

    private final UserServiceLogic userServiceLogic;
    private final JwtServiceLogic jwtServiceLogic;
    private final AuthenticationManager authenticationManager;
    private final AppSettingServiceLogic appSettingService;

    @Transactional
    @Override
    public SigninUpResponse signUp(SignUpRequest request) {
        var user = userServiceLogic.createUser(request);
        var jwt = jwtServiceLogic.generateToken(user.getUUID());
        appSettingService.initDefaultSettings(user);
        return new SigninUpResponse(
                jwt,
                user.getUUID(),
                UserToUserProfileMapper.map(user, appSettingService.getClientAppSettings(user), null)
        );
    }

    @Override
    public JWTResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUUID(), "")
        );
        // UUID as a username
        var user = userServiceLogic.userAuth().loadUserByUsername(request.getUUID());
        var jwt = jwtServiceLogic.generateToken(user.getUsername());
        return new JWTResponse(jwt);
    }
}
