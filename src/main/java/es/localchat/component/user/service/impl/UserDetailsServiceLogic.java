package es.localchat.component.user.service.impl;

import es.localchat.component.user.model.UserAuth;
import es.localchat.component.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceLogic implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        var user = userRepository.findByUUID(uuid);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("Could not found user.");
        }
        return new UserAuth(uuid);
    }
}
