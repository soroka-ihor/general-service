package es.localchat.component.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@Schema(description = "Registration request")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SignUpRequest {
    @NotEmpty
    String fullName;
    String currentLocation;
}
