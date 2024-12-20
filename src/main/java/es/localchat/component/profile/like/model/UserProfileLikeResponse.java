package es.localchat.component.profile.like.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileLikeResponse {
    private UUID userId;
    private UUID likedUserId;
    private boolean isLiked;
    private ZonedDateTime when;
}
