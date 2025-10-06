package com.iss.services;

import com.iss.models.UserEntity;
import com.iss.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;

    public Optional<UserEntity> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    public UserEntity save(UserEntity user)
    {
        return userRepository.save(user);
    }
}
