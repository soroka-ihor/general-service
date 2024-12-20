package es.localchat.component.user.about.model;

import es.local.chat.sharedentities.model.customabout.CustomUserAbout;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserAboutRequest {
    private List<CustomUserAbout> customUserAbout;
}
