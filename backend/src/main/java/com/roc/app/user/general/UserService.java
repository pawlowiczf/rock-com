package com.roc.app.user.general;

import com.roc.app.user.general.dto.UserResponseDto;
import com.roc.app.user.general.dto.UserUpdateRequestDto;
import com.roc.app.user.general.exception.EmailAlreadyInUseException;
import com.roc.app.user.general.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUserId(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    public String getUserRole(Integer userId) {
        User user = getUserByUserId(userId);
        return user.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_USER");
    }

    public UserResponseDto updateUser(User user, UserUpdateRequestDto dto){
        if (!user.getEmail().equals(dto.email()) && userRepository.findByEmail(dto.email()).isPresent()){
            throw new EmailAlreadyInUseException(dto.email());
        }
        if (dto.firstName() != null) user.setFirstName(dto.firstName());
        if (dto.lastName() != null) user.setLastName(dto.lastName());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.city() != null) user.setCity(dto.city());
        if (dto.phoneNumber() != null) user.setPhoneNumber(dto.phoneNumber());
        User savedUser = userRepository.save(user);
        return UserResponseDto.fromModel(savedUser);
    }
}
