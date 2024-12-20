package es.localchat.component.profile.like.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileLikeDTO {
    private UUID userId;
    private String fullName;
    private ZonedDateTime when;
    private Long avatarId;
}
