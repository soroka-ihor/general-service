package es.localchat.component.user.appsettings.mapper;

import es.local.chat.sharedentities.model.appsetting.ClientAppSettingValue;

import java.util.HashSet;
import java.util.Set;

public interface AppSettingValueMapper {

    static Object map(ClientAppSettingValue value) {
        try {
            return Long.parseLong(value.getValue());
        } catch (Exception e) {
            return value.getValue();
        }
    }

    static Set<Object> map(Set<ClientAppSettingValue> values) {
        Set<Object> valueSet = new HashSet<>();
        values.forEach(value -> valueSet.add(map(value)));
        return valueSet;
    }
}
