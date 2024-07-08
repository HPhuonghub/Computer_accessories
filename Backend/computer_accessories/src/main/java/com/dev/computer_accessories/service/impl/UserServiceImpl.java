package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.UserDTO;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.UserDetailResponse;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.Role;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.SearchRepository;
import com.dev.computer_accessories.repository.UserRepository;
import com.dev.computer_accessories.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final SearchRepository  searchRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleServiceImpl roleService;


    @Override
    public void saveUser(UserDTO userDTO) {
        if(existEmail(userDTO.getEmail())) {
            throw new ResourceNotFoundException("Email already exists");
        }

        Role role = roleService.findByName(userDTO.getRole().getName());

        User user = User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .status(userDTO.getStatus())
                .address(userDTO.getAddress())
                .role(role)
                .build();
        userRepository.save(user);

        log.info("Create user successfully!");

    }

    @Override
    public void updateUser(long userId, UserDTO userDTO) {
        Role role = roleService.findByName(userDTO.getRole().getName());

        User user = getUserById(userId);
        user.setFullName(userDTO.getFullName());
        if(StringUtils.hasLength(userDTO.getPhone())) {
            user.setPhone(userDTO.getPhone());
        }
        user.setStatus(userDTO.getStatus());
        user.setRole(role);
        user.setAddress(userDTO.getAddress());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);

        log.info("Update user successfully!");

    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById((Long) userId);

        log.info("Create user successfully!, userId = {}", userId);
    }

    @Override
    public void changePassword(long userId, UserDTO userSTO) {
        User user = getUserById(userId);
        user.setPassword(user.getPassword());
        userRepository.save(user);

    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = getUserById(userId);
        return UserDetailResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .address(user.getAddress())
                .build();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if(pageNo > 0) {
            p = pageNo - 1 ;
        }

        log.info("check pageSize: {}", pageSize);

        List<Sort.Order> sorts = new ArrayList<>();
        if(StringUtils.hasLength(sortBy)) {
            //fullName:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()) {
                if(matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }

            }
        }

        Pageable pageable = PageRequest.of(p, pageSize, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);

        List<UserDetailResponse> response = users.stream().map(user -> UserDetailResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .address(user.getAddress())
                        .role(user.getRole())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(users.getTotalPages())
                .items(response)
                .build();


    }

    @Override
    public PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        int p = 0;
        if(pageNo > 0) {
            pageNo = pageNo - 1 ;
        }

        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts) {
            //fullName:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()) {
                if(matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }

            }
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);
        List<UserDetailResponse> response = users.stream().map(user -> UserDetailResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(users.getTotalPages())
                .items(response)
                .build();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        return searchRepository.getAllUsersWithSortByColumnAndSearch(pageNo, pageSize, search, sortBy);
    }

    @Override
    public PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String... search) {
        return searchRepository.advanceSearchCriteria(pageNo, pageSize, sortBy, search);
    }


    public User getUserById(long userId) {
        return userRepository.findById((Long) userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public boolean existEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
