package es.localchat.component.auth.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {

    @NotBlank(message = "UUID cannot be blank")
    private String UUID;
}
