package com.uservault.service;

import com.uservault.dto.user.UpdateUserDTO;
import com.uservault.dto.user.UserCreatedDTO;
import com.uservault.enums.RoleType;
import com.uservault.exception.EmailInUseException;
import com.uservault.exception.EmailNotFoundException;
import com.uservault.exception.NotModifiedException;
import com.uservault.exception.UserNotFoundException;
import com.uservault.model.Role;
import com.uservault.model.User;
import com.uservault.repository.RoleRepository;
import com.uservault.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;

    private User updateUserCommon(User userToUpdate, UpdateUserDTO updateUserDTO, List<RoleType> roles) {
        User user = new User(userToUpdate);
        if (userRepository.findByEmail(updateUserDTO.getEmail()).isPresent()
                && !userToUpdate.getEmail().equalsIgnoreCase(updateUserDTO.getEmail())) {
            throw new EmailInUseException();
        }

        List<String> userRoles = Optional.ofNullable(roles)
                .map(roleTypes -> roleTypes.stream().map(Enum::name).toList())
                .orElse(userToUpdate.getRoles().stream().map(Role::getAuthority).toList());
        Set<Role> authorities = roleRepository.findAllByAuthorityIn(userRoles);

        userToUpdate.setEmail(updateUserDTO.getEmail() != null && !updateUserDTO.getEmail().isEmpty()
                ? updateUserDTO.getEmail() : userToUpdate.getEmail());
        userToUpdate.setName(updateUserDTO.getName() != null && !updateUserDTO.getName().isEmpty()
                ? updateUserDTO.getName() : userToUpdate.getName());
        if (!authorities.isEmpty()) {
            userToUpdate.setAuthorities(authorities);
        }

        if(user.equals(userToUpdate)){
            throw new NotModifiedException();
        }
        return userRepository.save(userToUpdate);
    }

    public UserCreatedDTO updateUser(String email, UpdateUserDTO updateUserDTO, List<RoleType> roles) {
        User updatedUser = userRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);
        User savedUser = updateUserCommon(updatedUser, updateUserDTO, roles);
        return new UserCreatedDTO(savedUser);
    }

    public UserCreatedDTO updateUser(UUID userId, UpdateUserDTO updateUserDTO, List<RoleType> roles) {

        User updatedUser = userRepository.findByUserId(userId).orElseThrow();
        User savedUser = updateUserCommon(updatedUser, updateUserDTO, roles);
        return new UserCreatedDTO(savedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public Page<UserCreatedDTO> getAllUsersDTO(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);

        return users.map(UserCreatedDTO::new);
    }

    public UserCreatedDTO getUserDTOByUuid(String id) {
        UUID userId = UUID.fromString(id);
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        return new UserCreatedDTO(user);
    }

    public UserCreatedDTO getUserDTOByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return new UserCreatedDTO(user);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public User getUserById(String id){
        UUID uuid = UUID.fromString(id);
        return userRepository.findByUserId(uuid).orElseThrow(UserNotFoundException::new);
    }

    public void deleteUserByEmail(String email){
        User user = this.getUserByEmail(email);
        user.setDeleted(true);
        userRepository.save(user);
    }
}

