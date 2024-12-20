package es.localchat.component.user.appsettings.service;

import es.local.chat.sharedentities.model.appsetting.ClientAppSettingType;
import es.local.chat.sharedentities.model.appsetting.ClientAppSettingValue;
import es.local.chat.sharedentities.model.user.UserEntity;
import es.localchat.component.user.appsettings.model.UpdateClientAppSettingResponse;


import java.util.Map;

public interface AppSettingService {

    void initDefaultSettings(UserEntity user);

    UpdateClientAppSettingResponse updateClientAppSetting(
            ClientAppSettingType type,
            ClientAppSettingValue value,
            UserEntity user
    );

    /**
     *
     * @param entity
     * @return map with setting type and its value. Value could be either String or Long
     */
    Map<ClientAppSettingType, Object> getClientAppSettings(UserEntity entity);
}
