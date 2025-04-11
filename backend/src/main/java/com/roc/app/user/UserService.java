package com.roc.app.user;

import com.roc.app.user.dto.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(user ->
                new UserResponseDto(
                        user.getUserId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName()
                )
        ).collect(Collectors.toList());
    }
}
