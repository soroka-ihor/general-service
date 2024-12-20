package es.localchat.component.user.appsettings.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateClientAppSettingRequest {
    String type;
    Object value;
}
