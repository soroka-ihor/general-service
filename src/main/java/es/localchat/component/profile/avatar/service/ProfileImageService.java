package es.localchat.component.profile.avatar.service;

import es.local.chat.sharedentities.model.user.UserEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {

    /**
     *
     * @param image
     * @param user
     * @return URL for getting a profile image by its id
     */
    String saveToDatabase(
            MultipartFile image,
            UserEntity user
    );

    /**
     *
     * @param id of its entry in the database.
     * @return Base64 encoded string.
     */
    String getById(long id);
}
