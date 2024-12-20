package es.localchat.component.user.appsettings.service.impl;

import es.local.chat.sharedentities.exception.ResourceNotFoundException;
import es.local.chat.sharedentities.exception.UnsupportedAppSettingValueException;
import es.local.chat.sharedentities.model.appsetting.ClientAppSetting;
import es.local.chat.sharedentities.model.appsetting.ClientAppSettingType;
import es.local.chat.sharedentities.model.appsetting.ClientAppSettingValue;
import es.local.chat.sharedentities.model.user.UserEntity;

import es.localchat.component.user.appsettings.mapper.AppSettingValueMapper;
import es.localchat.component.user.appsettings.model.UpdateClientAppSettingResponse;
import es.localchat.component.user.appsettings.repository.AppSettingRepository;
import es.localchat.component.user.appsettings.service.AppSettingService;
import es.localchat.websocket.service.WebsocketConnectionUpdater;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

import static es.local.chat.sharedentities.model.appsetting.ClientAppSettingType.OBSERVABLE_RADIUS;

@AllArgsConstructor
@Service
public class AppSettingServiceLogic implements AppSettingService {

    private final AppSettingRepository repository;

    @Transactional
    @Override
    public void initDefaultSettings(UserEntity user) {
        for (ClientAppSettingType type : ClientAppSettingType.values()) {
            var setting = new ClientAppSetting();
            setting.setUser(user);
            setting.setType(type);
            setting.setValue(type.getDefaultValue());
            repository.save(setting);
        }
    }

    @Override
    public UpdateClientAppSettingResponse updateClientAppSetting(ClientAppSettingType type, ClientAppSettingValue value, UserEntity user) {
        var setting = repository.findByUserAndType(user, type).orElseThrow(
                () -> new ResourceNotFoundException("No setting type " + type + " found for user " + user.getId().toString())
        );
        if (!type.getAvailableValues().contains(value)) {
            throw new UnsupportedAppSettingValueException("Unsupported value " + value + " for type " + type);
        }

//        if (type == OBSERVABLE_RADIUS) {
//            websocketConnectionUpdater.updateObservableRadius(user.getId(), Integer.parseInt(value.getValue()));
//        }

        setting.setValue(value);
        repository.save(setting);
        return new UpdateClientAppSettingResponse(
                user.getId().toString(),
                type.name(),
                value.getValue()
        );
    }

    @Override
    public Map<ClientAppSettingType, Object> getClientAppSettings(UserEntity entity) {
        return repository.findByUser(entity)
                .stream()
                .collect(Collectors.toMap(
                        ClientAppSetting::getType,
                        setting -> AppSettingValueMapper.map(setting.getValue())
                ));
    }
}
