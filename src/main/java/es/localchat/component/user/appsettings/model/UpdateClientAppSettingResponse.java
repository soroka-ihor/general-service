package es.localchat.component.user.appsettings.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateClientAppSettingResponse {
    String userId;
    String currentSettingType;
    String currentSettingValue;
}
