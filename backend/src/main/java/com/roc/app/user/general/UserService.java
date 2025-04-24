package com.roc.app.user.general;

import com.roc.app.user.general.dto.UserResponseDto;
import com.roc.app.user.general.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::fromModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto createUser(String firstName, String lastName, String email, String city, String phoneNumber) {
        User user = new User(firstName, lastName, email, city, phoneNumber);
        userRepository.save(user);
        return UserResponseDto.fromModel(user);
    }

    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
