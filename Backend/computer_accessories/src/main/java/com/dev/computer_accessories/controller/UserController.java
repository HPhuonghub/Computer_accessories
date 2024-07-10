package com.dev.computer_accessories.controller;


import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.dto.request.UserDTO;
import com.dev.computer_accessories.dto.response.ResponseError;
import com.dev.computer_accessories.dto.response.UserDetailResponse;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@Validated
@Tag(name="User Controller")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    @Operation(summary = "Get list of users per page", description = "Send a request via this API to get user list by pageNo and pageSize")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData<?> getAllUser(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                      @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                      @RequestParam(required = false) String sortBy) {
        log.info("Request get all of users");
        return new ResponseData<>(HttpStatus.OK.value(), "Get all user successful", userService.getAllUsersWithSortBy(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Get list of users with sort by multiple columns", description = "Send a request via this API to get user list by pageNo, pageSize and sort by multiple columns")
    @GetMapping("/list-with-sort-by-multiple-columns")
    public ResponseData<?> getAllUsersWithSortByMultipleColumns(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                             @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                             @RequestParam(required = false) String... sort) {
        log.info("Request get all of users with sort by multiple columns");
        return new ResponseData<>(HttpStatus.OK.value(), "Get all user successful", userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sort));
    }

    @Operation(summary = "Get list of users with sort by columns and search", description = "Send a request via this API to get user list by pageNo, pageSize, search and sort by search")
    @GetMapping("/list-with-sort-by-multiple-columns-search")
    public ResponseData<?> getAllUsersWithSortByColumnAndSearch(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                @RequestParam(required = false) String search,
                                                                @RequestParam(required = false) String sortBy) {
        log.info("Request get all of users with sort by multiple columns and search");
        return new ResponseData<>(HttpStatus.OK.value(), "Get all user successful", userService.getAllUsersWithSortByColumnAndSearch(pageNo, pageSize, search, sortBy));
    }

//    @Operation(summary = "Get list of users with sort by columns and search", description = "Send a request via this API to get user list by pageNo, pageSize, search and sort by search")
//    @GetMapping("/list-with-sort-by-multiple-columns-search")
//    public ResponseData<?> advanceSearchCriteria(@RequestParam(defaultValue = "0", required = false) int pageNo,
//                                                                @RequestParam(defaultValue = "20", required = false) int pageSize,
//                                                                @RequestParam(required = false) String sortBy,
//                                                                @RequestParam(required = false) String... search) {
//        log.info("Request advance search query by criteria");
//        return new ResponseData<>(HttpStatus.OK.value(), "Get all user successful", userService.advanceSearchByCriteria(pageNo, pageSize, sortBy, search));
//    }

    @Operation(summary = "Get user by id", description = "Return user by id")
    @GetMapping("/{userId}")
    public ResponseData<UserDetailResponse> getUserId(@PathVariable long userId) {
        log.info("Request get user by id, userId = {}", (Long) userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "user", userService.getUser(userId));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Add user", description = "API create new user")
    @PostMapping("/")
    public ResponseData<Long> addUser(@Valid @RequestBody UserDTO user) {
        log.info("Request add user: {}", user.getFullName());
        try {userService.saveUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully");
        } catch(Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @Operation(summary = "Put user", description = "API edit user")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@Min(value = 1, message = "userId must be greater than 0") @PathVariable long userId, @RequestBody UserDTO userDTO) {
        log.info("Request update userId = {}", (Long) userId);
        try {
            userService.updateUser(userId, userDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update a user successfully");
        } catch (ResourceNotFoundException e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");
        }

    }

    @Operation(summary = "Patch user", description = "API patch user")
    @PatchMapping("/{userId}")
    public ResponseData<?> changePassword(@Min(value = 1 , message = "userId must be greater than 0") @PathVariable long userId, @RequestParam UserDTO userDTO) {
        log.info("Request update password userId = {}", (Long) userId);
        try {
            userService.changePassword(userId, userDTO);
            return  new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update password successfully");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update password fail");
        }

    }

    @Operation(summary = "Delete user", description = "API delete user")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable long userId) {
        log.info("Request delete userId = {}", (Long) userId);
        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete a user successfully");
        } catch (Exception e) {
            log.error("errorMessage = {}",e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user fail");
        }

    }
}
