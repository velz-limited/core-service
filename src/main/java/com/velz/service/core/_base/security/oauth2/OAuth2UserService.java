package com.velz.service.core._base.security.oauth2;

import com.velz.service.core._base.security.oauth2.userinfo.OAuth2UserInfo;
import com.velz.service.core._base.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.velz.service.core.user.User;
import com.velz.service.core.user.UserService;
import com.velz.service.core.user.request.UserOAuth2SignUpRequest;
import com.velz.service.core.user.request.UserOAuth2UpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oauth2UserRequest) throws OAuth2AuthenticationException {
        try {
            return processOAuth2User(oauth2UserRequest, super.loadUser(oauth2UserRequest));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oauth2UserRequest, OAuth2User oauth2User) {
        OAuth2UserInfo oauth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oauth2UserRequest.getClientRegistration().getRegistrationId(),
                oauth2User.getAttributes());

        Optional<User> userByProvider = userService.oauth2findById(oauth2UserInfo.getProvider(), oauth2UserInfo.getId());
        if (userByProvider.isPresent()) {
            if (userByProvider.get().getEmail().equalsIgnoreCase(oauth2UserInfo.getEmail())) {
                return userByProvider.get();
            } else {
                Optional<User> userByEmail = userService.findByEmail(oauth2UserInfo.getEmail());
                if (userByEmail.isPresent()) {
                    return userByProvider.get();
                } else {
                    return updateUser(userByProvider.get(), oauth2UserInfo);
                }
            }
        } else {
            // Hijacking via email is possible here. However, most OAuth2 providers only give verified emails.
            Optional<User> userByEmail = userService.findByEmail(oauth2UserInfo.getEmail());
            if (userByEmail.isPresent()) {
                return updateUser(userByEmail.get(), oauth2UserInfo);
            } else {
                return signUpUser(oauth2UserInfo);
            }
        }
    }

    private User signUpUser(OAuth2UserInfo oauth2UserInfo) {
        UserOAuth2SignUpRequest request = new UserOAuth2SignUpRequest();
        request.setProvider(oauth2UserInfo.getProvider());
        request.setId(oauth2UserInfo.getId());
        request.setEmail(oauth2UserInfo.getEmail());
        String displayName = oauth2UserInfo.getName();
        if (StringUtils.isNotEmpty(displayName)) {
            request.setDisplayName(displayName);
        } else {
            request.setDisplayName(userService.generateDisplayName());
        }
        request.setUsername(userService.generateUsername());
        return userService.oauth2SignUp(request);
    }

    private User updateUser(User user, OAuth2UserInfo oauth2UserInfo) {
        UserOAuth2UpdateRequest request = new UserOAuth2UpdateRequest();
        request.setProvider(oauth2UserInfo.getProvider());
        request.setId(oauth2UserInfo.getId());
        String email = oauth2UserInfo.getEmail();
        if (StringUtils.isEmpty(email)) {
            request.setEmail(user.getEmail());
        } else {
            request.setEmail(oauth2UserInfo.getEmail());
        }
        return userService.oauth2Update(user, request);
    }
}
