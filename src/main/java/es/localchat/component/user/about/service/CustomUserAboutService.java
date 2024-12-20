package es.localchat.component.user.about.service;

import es.local.chat.sharedentities.model.customabout.CustomUserAbout;
import es.local.chat.sharedentities.model.user.UserEntity;

import java.util.List;
import java.util.Set;

public interface CustomUserAboutService {
    void updateUsersCustomAbouts(UserEntity user, Set<String> customAbout);
    void deleteCustomAbout2User(UserEntity user, CustomUserAbout customUserAbout);
    List<CustomUserAbout> getAllUserCustomAbout(UserEntity user);
    void deleteCustomAboutIfNoMoreUsersUseIt(CustomUserAbout customUserAbout);
    int amountOfUsersUsedCustomAbout(String customAbout);
    void insertNewCustomIfNotUsedAlready(String customAbout, UserEntity user);
}
