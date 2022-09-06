package com.example.TelegramBot.api.repositories;

import com.example.TelegramBot.api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findByChatId(Long chatId);

    public boolean existsByChatId(Long chatId);

    @Modifying
    @Query("update UserEntity u set u.finishedSub = :finishedSub where u.chatId = :chatId")
    @Transactional
    public void updateUserDate(@Param(value = "chatId") Long id, @Param(value = "finishedSub") Timestamp finishedSub);
}
