package es.localchat.component.profile.avatar.service.impl;

import es.local.chat.sharedentities.model.profileimage.ProfileImageEntity;
import es.local.chat.sharedentities.model.user.UserEntity;
import es.localchat.component.profile.avatar.repository.ProfileImageRepo;
import es.localchat.component.profile.avatar.service.ProfileImageService;
import es.localchat.component.user.service.impl.UserServiceLogic;
import es.localchat.constant.ApiPaths;
import es.localchat.utils.MultipartUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
@AllArgsConstructor
public class ProfileImageServiceLogic implements ProfileImageService {

    private final Logger logger = LoggerFactory.getLogger(ProfileImageServiceLogic.class);
    private final ProfileImageRepo repo;
    private final UserServiceLogic userServiceLogic;

    @Override
    @Transactional
    public String saveToDatabase(MultipartFile image, UserEntity user) {
        try {
            byte[] shrinkedProfileImage = MultipartUtils.shrinkUp(image);
            incrementProfilePicturesSequences(user);
            var avatar = new ProfileImageEntity(
                    null,
                    shrinkedProfileImage,
                    1,
                    LocalDateTime.now(),
                    user
            );
            repo.save(avatar);
            userServiceLogic.updateHash(user);
            return String.format(
                    "%s/avatar/%d", ApiPaths.V1_MAPPING, avatar.getId()
            );
        } catch (Exception e) {
            repo.resetSequence();
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getById(long id) {
        var profileImageOpt = repo.findById(id);
        if (profileImageOpt.isEmpty()) {
            //TODO: Replace with appropriate exception
            throw new RuntimeException("You are trying to access an unknown to you resource.");
        }
        return Base64.getEncoder().encodeToString(profileImageOpt.get().getData());
    }

    private void incrementProfilePicturesSequences(UserEntity user) {
        user.getProfileImages().forEach(
                p -> p.setSequence(p.getSequence() + 1)
        );
    }
}
