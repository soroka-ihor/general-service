package es.localchat.component.profile.like.controller;

import es.localchat.component.profile.like.model.UserProfileLikeDTO;
import es.localchat.component.profile.like.model.UserProfileLikeResponse;
import es.localchat.component.profile.like.service.UserProfileLikeService;
import es.localchat.constant.ApiPaths;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(ApiPaths.V1_MAPPING + ApiPaths.PROFILE_LIKE_MAPPING)
public class UserProfileLikeController {

    private final UserProfileLikeService service;

    @PutMapping("/{likedUserId}")
    public ResponseEntity<UserProfileLikeResponse> like(
            @PathVariable UUID likedUserId
    ) {
        return ResponseEntity.ok(service.likeProfile(likedUserId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<UserProfileLikeDTO>> myLikes() {
        return ResponseEntity.ok(service.getMyLikes());
    }

    @GetMapping("/likeme")
    public ResponseEntity<List<UserProfileLikeDTO>> likeMe() {
        return ResponseEntity.ok(service.getUsersWhoLikeMyProfile());
    }
}
