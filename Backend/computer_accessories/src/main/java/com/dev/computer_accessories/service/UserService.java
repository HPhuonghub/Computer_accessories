package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.UserDTO;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.UserDetailResponse;

public interface UserService {

    void saveUser(UserDTO userDTO);

    void updateUser(long userId, UserDTO userDTO);

    void deleteUser(long userId);

    void changePassword(long userId, UserDTO userSTO);

    UserDetailResponse getUser(long userId);

    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);

    PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);

    PageResponse<?> getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy);

    PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String... search );
}
