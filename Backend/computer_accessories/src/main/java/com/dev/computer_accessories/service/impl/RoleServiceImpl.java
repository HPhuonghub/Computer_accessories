package com.dev.computer_accessories.service.impl;


import com.dev.computer_accessories.exception.AppException;
import com.dev.computer_accessories.exception.ErrorCode;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.Role;
import com.dev.computer_accessories.repository.RoleRepository;
import com.dev.computer_accessories.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }
}
