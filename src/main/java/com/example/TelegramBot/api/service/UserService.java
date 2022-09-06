package com.example.TelegramBot.api.service;

import com.example.TelegramBot.api.entities.UserEntity;
import com.example.TelegramBot.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(UserEntity user) {
        userRepository.save(user);
    }

    public UserEntity updateUser(Long chatId, UserEntity newUser) {
        newUser.setChatId(chatId);
        return userRepository.save(newUser);
    }

    public void updateUserDate (Long chatId, Timestamp timestamp) {
        userRepository.updateUserDate(chatId, timestamp);
    }

    public UserEntity findById (Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public boolean existsById (Long chatId) {
        return userRepository.existsByChatId(chatId);
    }

    public void deleteUser(Long chatId) {
        userRepository.deleteById(chatId);
    }
}
