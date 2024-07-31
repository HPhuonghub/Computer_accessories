package com.dev.computer_accessories.oauth2;

import com.dev.computer_accessories.exception.OAuth2AuthenticationProcessingException;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.AuthProvider;
import com.dev.computer_accessories.model.Role;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.oauth2.user.OAuth2UserInfo;
import com.dev.computer_accessories.oauth2.user.OAuth2UserInfoFactory;
import com.dev.computer_accessories.oauth2.user.UserPrincipal;
import com.dev.computer_accessories.repository.RoleRepository;
import com.dev.computer_accessories.repository.UserRepository;
import com.dev.computer_accessories.service.impl.RoleServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final RoleServiceImpl roleService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        System.out.println("--------------------------");
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            System.out.println("------------------------------");
            System.out.println(oAuth2User);
            System.out.println(oAuth2UserRequest);
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(
                        oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes()
                );
        System.out.println("before 52");
        System.out.println(oAuth2UserInfo.getEmail());
        System.out.println(oAuth2UserInfo.getId());
        System.out.println(oAuth2UserInfo.getName());
        if (oAuth2UserInfo.getEmail() == null) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        System.out.println("before 57" + oAuth2UserRequest);
        AuthProvider provider = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        Optional<User> userOptional = userRepository.findByEmailAndProvider(oAuth2UserInfo.getEmail(), provider);
        if(userOptional.isPresent()) {
            System.out.println("check userOptional successful");
        }
        else {
            System.out.println("check userOptional failed");
        }
        System.out.println("----------------------------");
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            System.out.println(user);
            System.out.println(oAuth2UserRequest.getClientRegistration());
            System.out.println(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
            System.out.println("checkout");
            System.out.println(user.getProvider().toString().toLowerCase());
            String checkProvider = String.valueOf(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
            if (checkProvider.isEmpty()) {
                throw new ResourceNotFoundException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            if(!user.getProvider().toString().toUpperCase().equalsIgnoreCase(checkProvider)) {
                user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
            log.info("check user: {}", user.getRole().getName());
        }
        System.out.println("check user successful ");
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }


    public User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Role role = roleService.findByName("USER");

        User user = User.builder()
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()))
                .providerId(oAuth2UserInfo.getId())
                .fullName(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .imageUrl(oAuth2UserInfo.getImageUrl())
                .role(role)
                .enabled(true)
                .build();
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFullName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
