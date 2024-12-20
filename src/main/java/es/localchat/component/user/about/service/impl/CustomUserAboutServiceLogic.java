package es.localchat.component.user.about.service.impl;

import es.local.chat.sharedentities.exception.CustomUserAboutExceededException;
import es.local.chat.sharedentities.model.customabout.CustomUserAbout;
import es.local.chat.sharedentities.model.customabout.CustomUserAbout2User;
import es.local.chat.sharedentities.model.user.UserEntity;
import es.localchat.component.user.about.repository.CustomUserAbout2UserRepository;
import es.localchat.component.user.about.repository.CustomUserAboutRepository;
import es.localchat.component.user.about.service.CustomUserAboutService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomUserAboutServiceLogic implements CustomUserAboutService {

    private static final int CUSTOM_USER_ABOUT_THRESHOLD = 10;
    private static final int CUSTOM_USER_ABOUT_SIZE = 30;
    CustomUserAboutRepository repository;
    CustomUserAbout2UserRepository about2UserRepository;
    CustomUserAboutRepository customUserAboutRepository;

    @Override
    @Transactional
    public void updateUsersCustomAbouts(UserEntity user, Set<String> requestedCustomAbouts)
            throws CustomUserAboutExceededException {

            checkIfRequestedCustomAboutsNotViolateThreshold(requestedCustomAbouts);
            checkIfRequestedCustomAboutsNotViolateSize(requestedCustomAbouts);

            List<CustomUserAbout> customUserAbouts =
                    customUserAboutRepository.findAllByUserId(user.getId());

            if (!requestedCustomAbouts.isEmpty()) {
                requestedCustomAbouts.forEach(
                        requestedCustomAboutText -> insertNewCustomIfNotUsedAlready(requestedCustomAboutText, user)
                );
            }

            customUserAbouts.forEach(
                    customUserAbout -> {
                        if (!requestedCustomAbouts.contains(customUserAbout.getText())) {
                            deleteCustomAbout2User(user, customUserAbout);
                            deleteCustomAboutIfNoMoreUsersUseIt(customUserAbout);
                        }
                    }
            );
    }

    @Override
    public void deleteCustomAbout2User(UserEntity user, CustomUserAbout customUserAbout) {
        var customAbout2User = about2UserRepository.findByUserIdAndCustomUserAboutId(user.getId(), customUserAbout.getId());
        customAbout2User.ifPresent(about2UserRepository::delete);
    }

    @Override
    public List<CustomUserAbout> getAllUserCustomAbout(UserEntity user) {
        return repository.findAllByUserId(user.getId());
    }

    @Override
    public int amountOfUsersUsedCustomAbout(String customAbout) {
        return customUserAboutRepository.countOfUsersUsedCustomAbout(customAbout);
    }

    @Override
    public void insertNewCustomIfNotUsedAlready(String customAboutText, UserEntity user) {
        Optional<CustomUserAbout> customUserAboutOptional = repository.findByUserIdAndText(user.getId(), customAboutText);
        if (!customUserAboutOptional.isPresent()) {
            var customAbout = new CustomUserAbout(null, customAboutText);
            customUserAboutRepository.save(customAbout);
            var customAbout2user = new CustomUserAbout2User(null, customAbout.getId(), user.getId());
            about2UserRepository.save(customAbout2user);
        }
    }

    @Override
    public void deleteCustomAboutIfNoMoreUsersUseIt(CustomUserAbout customUserAbout) {
        if (amountOfUsersUsedCustomAbout(customUserAbout.getText()) == 0) {
            customUserAboutRepository.delete(customUserAbout);
        }
    }

    private void checkIfRequestedCustomAboutsNotViolateThreshold(Set<String> requestedCustomAbouts) {
        if (requestedCustomAbouts.size() > CUSTOM_USER_ABOUT_THRESHOLD) {
            throw new CustomUserAboutExceededException(
                    String.format("Exceeded threshold of %d of custom user abouts.", CUSTOM_USER_ABOUT_THRESHOLD)
            );
        }
    }

    private void checkIfRequestedCustomAboutsNotViolateSize(Set<String> requestedCustomAbouts) {
        requestedCustomAbouts.forEach(
                requestedCustomAboutText -> {
                    if (requestedCustomAboutText.length() > CUSTOM_USER_ABOUT_SIZE) {
                        throw new CustomUserAboutExceededException(
                                String.format("Exceeded threshold of %d of custom about size.", CUSTOM_USER_ABOUT_SIZE)
                        );
                    }
                }
        );
    }
}
