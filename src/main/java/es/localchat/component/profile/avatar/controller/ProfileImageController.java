package es.localchat.component.profile.avatar.controller;

import es.localchat.component.profile.avatar.service.impl.ProfileImageServiceLogic;
import es.localchat.component.user.service.impl.UserServiceLogic;
import es.localchat.constant.ApiPaths;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiPaths.V1_MAPPING + ApiPaths.AVATAR_MAPPING)
@AllArgsConstructor
public class ProfileImageController {

    private final UserServiceLogic userServiceLogic;
    private final ProfileImageServiceLogic avatarServiceLogic;

    @GetMapping("/{id}")
    public ResponseEntity<String> getProfileImage(
            @PathVariable("id") long id
    ) {
        return ResponseEntity.ok(avatarServiceLogic.getById(id));
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> update(
            @RequestParam MultipartFile avatar
    ) {
        return ResponseEntity.ok().body(
                avatarServiceLogic.saveToDatabase(avatar, userServiceLogic.getAuthenticatedUser())
        );
    }
}
