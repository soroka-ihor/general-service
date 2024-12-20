package es.localchat.component.user.service.impl;

import es.local.chat.sharedentities.exception.ResourceNotFoundException;
import es.local.chat.sharedentities.exception.UsernameAlreadyExistsException;
import es.local.chat.sharedentities.model.user.About;
import es.local.chat.sharedentities.model.user.UserEntity;

import es.localchat.component.auth.model.SignUpRequest;
import es.localchat.component.profile.like.repository.UserProfileLikeRepository;
import es.localchat.component.user.about.service.CustomUserAboutService;
import es.localchat.component.user.appsettings.service.impl.AppSettingServiceLogic;
import es.localchat.component.user.mapper.UserToUserProfileMapper;
import es.localchat.component.user.model.*;
import es.localchat.component.user.repository.UserRepository;
import es.localchat.component.user.service.UserService;
import es.localchat.websocket.service.WebsocketConnectionUpdater;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceLogic implements UserService {

    private final UserRepository repo;
    private final GeometryFactory geometryFactory;
    private final AppSettingServiceLogic clientAppSettingLogic;
    private final CustomUserAboutService customUserAboutService;

    public UserServiceLogic(UserRepository repo,
                            AppSettingServiceLogic clientAppSettingLogic,
                            CustomUserAboutService customUserAboutService
    ) {
        this.repo = repo;
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        this.clientAppSettingLogic = clientAppSettingLogic;
        // TODO: Check here
//        this.connectionUpdater = connectionUpdater;
        this.customUserAboutService = customUserAboutService;
    }

    @Override
    public UserDetailsService userAuth() {
        return this::getUserByUUID;
    }

    @Override
    public MyUserProfileDTO getCurrentUserProfile() {
        var currentUser = getAuthenticatedUser();
        var userProfile = UserToUserProfileMapper.map(
                currentUser,
                clientAppSettingLogic.getClientAppSettings(currentUser),
                customUserAboutService.getAllUserCustomAbout(currentUser)
        );
        return userProfile;
    }

    @Override
    public UserProfileDTO getUserProfileById(UUID userId) {
        var currentUser = getAuthenticatedUser();
        var requestedUser = getById(userId);
        if (requestedUser == null) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }
        return buildUserProfileDTO(currentUser, requestedUser);
    }

    public UserProfileDTO getUserProfileByIds(UUID currentUserId, UUID userId) {
        var currentUser = repo.getFullUserById(currentUserId);
        var requestedUser = repo.getFullUserById(userId);
        if (currentUser.isEmpty() || requestedUser.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format(
                            "User ID(s) %s not found",
                            (currentUser.isEmpty() ? currentUserId.toString() : " ") + (requestedUser.isEmpty() ? userId : " ")
                    )
            );
        }
        return buildUserProfileDTO(currentUser.get(), requestedUser.get());
    }

    private UserProfileDTO buildUserProfileDTO(UserEntity currentUser, UserEntity requestedUser) {
        return UserToUserProfileMapper.mapToDto(
                requestedUser,
                clientAppSettingLogic.getClientAppSettings(requestedUser),
                customUserAboutService.getAllUserCustomAbout(requestedUser)
        );
    }

    @Override
    public UpdateUsernameResponse updateUsername(UpdateUsernameRequest request) {
        var currentUser = getAuthenticatedUser();
        var username = request.getUsername().get();
        if (repo.existsByUsername(username) && !currentUser.getUsername().equals(username)) {
            throw new UsernameAlreadyExistsException(String.format("Username '%s' already exists.", username));
        }
        currentUser.setUsername(username);
        updateHash(currentUser);
        return new UpdateUsernameResponse(currentUser.getId().toString(), currentUser.getUsername());
    }

    @Override
    public UpdateFullNameResponse updateFullName(UpdateFullNameRequest request) {
        var currenUser = getAuthenticatedUser();
        var fullName = request.getFullName();
        currenUser.setFullName(fullName);
        updateHash(currenUser);
        return new UpdateFullNameResponse(currenUser.getId().toString(), fullName);
    }

    @Override
    public UserEntity getAuthenticatedUser() {
        var uuid = SecurityContextHolder.getContext().getAuthentication().getName();
        return repo.findByUUID(uuid).orElseThrow(() -> new UsernameNotFoundException(uuid));
    }

    @Override
    public UserEntity createUser(SignUpRequest request) {
        Double latitude = Double.valueOf(request.getCurrentLocation().split(",")[0]);
        Double longitude = Double.valueOf(request.getCurrentLocation().split(",")[1]);
        var user = new UserEntity(
                null,
                UUID.randomUUID().toString(),
                null,
                request.getFullName(),
                null,
                LocalDateTime.now(),
                geometryFactory.createPoint(
                        new Coordinate(longitude, latitude)
                ),
                Boolean.FALSE,
                ZonedDateTime.now(),
                null,
                Collections.emptyList(),
                Collections.emptySet()
        );
        repo.save(user);
        return updateHash(user);
    }

    @Override
    @Transactional
    public UpdateUserAboutResponse updateAbout(Set<About> about, Set<String> customAbout) {
        var currentUser = getAuthenticatedUser();
        customUserAboutService.updateUsersCustomAbouts(currentUser, customAbout);
        currentUser.setAbout(about);
        updateHash(currentUser);
        return new UpdateUserAboutResponse(
                currentUser.getId().toString(),
                currentUser.getAbout()
        );
    }

    @Override
    public CurrentPositionUpdateResponse updateCurrentPosition(CurrentPositionUpdateRequest request) {
        var point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
        );
        var currentUser = getAuthenticatedUser();
        currentUser.setCurrentPoint(point);
        updateHash(currentUser);
        // TODO: Check it
        //connectionUpdater.updateConnectionLocation(currentUser.getId(), request.getLongitude(), request.getLatitude());
        return new CurrentPositionUpdateResponse(
                currentUser.getId().toString(),
                // Y stands for latitude
                // X stands for longitude
                currentUser.getCurrentPoint().getY(),
                currentUser.getCurrentPoint().getX()
        );
    }

    @Override
    public String getUserHash(String userId) {
        var user = getById(UUID.fromString(userId));
        if (user != null) {
            return user.getHash();
        }
        throw new ResourceNotFoundException("User with id " + userId + " not found");
    }

    // TODO: Debug stuff. Delete before production.
    @Override
    public CurrentPositionUpdateResponse updateCurrentPosition(CurrentPositionUpdateRequest request, UserEntity user) {
        var point = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude())
        );
        var currentUser = user;
        currentUser.setCurrentPoint(point);
        repo.save(currentUser);
        // TODO: Check it
        // consider calling location update in localchat-service
        // connectionUpdater.updateConnectionLocation(currentUser.getId(), request.getLongitude(), request.getLatitude());
        return new CurrentPositionUpdateResponse(
                currentUser.getId().toString(),
                // Y stands for latitude
                // X stands for longitude
                currentUser.getCurrentPoint().getY(),
                currentUser.getCurrentPoint().getX()
        );
    }

    public UserEntity getUserByUUID(UUID uuid) {
        return repo.findByUUID(uuid.toString()).orElseThrow(() -> new UsernameNotFoundException(uuid.toString()));
    }

    public UserAuth getUserByUUID(String uuid) {
        UserEntity user = repo.findByUUID(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User with UUID " + uuid + " not found"));
        return new UserAuth(user.getUUID());
    }

    @Override
    public UserEntity getById(UUID userId) {
        return repo.findById(userId).orElseThrow(() -> new UsernameNotFoundException(userId.toString()));
    }

    @Override
    public UserEntity updateHash(UserEntity user) {
        var userHash = user.calculateHash(
                user.getId(),
                Optional.ofNullable(user.getUsername()).orElse(""),
                user.getFullName(),
                Optional.ofNullable(user.getAbout()).orElse(Collections.emptySet()),
                user.getCustomAbout(),
                user.getProfileImages()
        );
        user.setHash(userHash);
        return repo.save(user);
    }

    @Override
    public Double distanceBetweenTwoUsers(UserEntity user1, UserEntity user2) {
        return repo.findDistanceBetween(user1.getId(), user2.getId());
    }

    @Override
    public void makeOnline(UserEntity user) {
        user.setIsOnline(Boolean.TRUE);
        user.setRecentActivity(ZonedDateTime.now(ZoneId.of("UTC")));
        repo.save(user);
    }

    @Override
    public void makeOffline(UserEntity user) {
        user.setIsOnline(Boolean.FALSE);
        user.setRecentActivity(ZonedDateTime.now(ZoneId.of("UTC")));
        repo.save(user);
    }

    @Override
    public OnlineStatus onlineStatus(UUID userId) {
        var onlineStatus = repo.getOnlineStatus(userId).orElseThrow(
                () -> new UsernameNotFoundException("User with UUID " + userId + " not found")
        );
        if (onlineStatus.getOnline()) {
            onlineStatus.setLastSeen(null);
        }
        return onlineStatus;
    }

    @Override
    public Double getDistanceToUser(UUID userId) {
        var authenticatedUser = getAuthenticatedUser();
        var requestedUser = repo.findById(userId).orElseThrow(() -> new UsernameNotFoundException(userId.toString()));
        return distanceBetweenTwoUsers(authenticatedUser, requestedUser);
    }
}
