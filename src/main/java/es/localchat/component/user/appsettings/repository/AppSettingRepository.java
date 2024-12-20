package es.localchat.component.user.appsettings.repository;

import es.local.chat.sharedentities.model.appsetting.ClientAppSetting;
import es.local.chat.sharedentities.model.appsetting.ClientAppSettingType;
import es.local.chat.sharedentities.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppSettingRepository extends JpaRepository<ClientAppSetting, Long> {
    Optional<ClientAppSetting> findByUserAndType(UserEntity user, ClientAppSettingType type);
    List<ClientAppSetting> findByUser(UserEntity user);
}
