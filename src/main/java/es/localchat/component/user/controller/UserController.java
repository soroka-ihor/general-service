package es.localchat.component.user.controller;

import es.local.chat.sharedentities.model.appsetting.ClientAppSettingType;
import es.local.chat.sharedentities.model.appsetting.ClientAppSettingValue;
import es.local.chat.sharedentities.model.user.About;
import es.localchat.component.user.appsettings.mapper.AppSettingValueMapper;
import es.localchat.component.user.appsettings.model.UpdateClientAppSettingRequest;
import es.localchat.component.user.appsettings.model.UpdateClientAppSettingResponse;
import es.localchat.component.user.appsettings.service.AppSettingService;
import es.localchat.component.user.model.*;
import es.localchat.component.user.service.impl.UserServiceLogic;
import es.localchat.constant.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping( ApiPaths.V1_MAPPING + ApiPaths.USER_MAPPING)
@AllArgsConstructor
public class UserController {

    private final UserServiceLogic service;
    private final AppSettingService settingService;

    // caching at frontend
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getUserProfileById(userId));
    }

    // TODO: Add caching here
    @GetMapping({"/{userId}/onlineStatus"})
    public ResponseEntity<OnlineStatus> isOnline(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.onlineStatus(userId));
    }

    @GetMapping({"/{userId}/distance"})
    public ResponseEntity<Double> getDistanceToUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getDistanceToUser(userId));
    }

    @GetMapping("/current")
    public ResponseEntity<MyUserProfileDTO> getCurrentUserProfile() {
        return ResponseEntity.ok(service.getCurrentUserProfile());
    }

    @PutMapping("/username/update")
    public ResponseEntity<UpdateUsernameResponse> updateUserProfile(
            @Valid @RequestBody UpdateUsernameRequest request
    ) {
        return ResponseEntity.ok(service.updateUsername(request));
    }

    @PutMapping("/fullname/update")
    public ResponseEntity<UpdateFullNameResponse> updateFullName(
            @RequestBody UpdateFullNameRequest request
    ) {
        return ResponseEntity.ok(service.updateFullName(request));
    }


    @Operation(summary = "Updates either pre-defined user's about or custom user abouts, or both of them.",
            description = "One user could have no more than 10 custom abouts. Allowed length for a custom about is " +
                          "no more than 30 symbols including emojis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Abouts successfully updated."),
            @ApiResponse(responseCode = "409", description = "An about length or their amount were violated.")
    })
    @PutMapping("/about")
    public ResponseEntity<String> updateAbout(
            @RequestBody UpdateUserAboutRequest request
    ) {
        service.updateAbout(request.getAbout(), request.getCustomAbout());
        return ResponseEntity.ok("Abouts updated.");
    }

    @GetMapping("/about")
    public ResponseEntity<List<About>> getUserAbouts() {
        return ResponseEntity.ok(
                List.of(About.values())
        );
    }

    @GetMapping("/settings/list")
    public ResponseEntity<Map<ClientAppSettingType, Set<Object>>> getSettings() {
        return ResponseEntity.ok(
                Stream.of(ClientAppSettingType.values())
                        .collect(Collectors.toMap(
                                type -> type,
                                type -> AppSettingValueMapper.map(type.getAvailableValues())
                        ))
        );
    }

    @PostMapping("/settings/update")
    @Operation(summary = "Updates setting value for a given setting type.",
            description = "Corresponding setting type and their values you could find at /settings/list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setting value successfully updated."),
            @ApiResponse(responseCode = "400", description = "Either wrong setting type, or wrong setting value.")
    })
    public ResponseEntity<UpdateClientAppSettingResponse> updateClientAppSetting(
            @RequestBody UpdateClientAppSettingRequest request
    ) {
        return ResponseEntity.ok(
                settingService.updateClientAppSetting(
                        ClientAppSettingType.valueOf(request.getType()),
                        ClientAppSettingValue.of(request.getValue()),
                        service.getAuthenticatedUser()
                )
        );
    }

    @PutMapping("/position/update")
    public ResponseEntity<CurrentPositionUpdateResponse> updateCurrentPosition(
            @Valid @RequestBody CurrentPositionUpdateRequest request
    ) {
        return ResponseEntity.ok(
                service.updateCurrentPosition(request)
        );
    }
}
