package com.ayni.backend.user.service;

import com.ayni.backend.user.dto.ChangePasswordRequest;
import com.ayni.backend.user.dto.UpdateUserProfileRequest;
import com.ayni.backend.user.dto.UserProfileResponse;

public interface UserService {

    UserProfileResponse getMyProfile();

    UserProfileResponse updateMyProfile(UpdateUserProfileRequest request);

    void changePassword(ChangePasswordRequest request);
}