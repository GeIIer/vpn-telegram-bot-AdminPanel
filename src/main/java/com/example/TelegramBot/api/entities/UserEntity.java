package com.example.TelegramBot.api.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
@Getter
@Setter
@Table(name = "usersDataTable")
@ToString
public class UserEntity {
    @Id
    private Long chatId;

    private String firstName;

    private String lastName;

    private String userName;

    private Timestamp registeredAt;
    private Timestamp startedSub;
    private Timestamp finishedSub;
    private boolean freeSub;
    private String accessKey;
    private int idKey;
    private int referralUsers;

    public void setFirstNameIfNotNull(String firstName) {
        if(firstName != null){
            this.firstName = firstName;
        }
    }

    public void setLastNameIfNotNull(String lastName) {
        if(lastName != null) {
            this.lastName = lastName;
        }
    }

    public void setUserNameIfNotNull(String userName) {
        if(userName != null) {
            this.userName = userName;
        }
    }

    public void setRegisteredAtIfNotNull(Timestamp registeredAt) {
        if(registeredAt != null) {
            this.registeredAt = registeredAt;
        }
    }

    public void setFinishedSubIfNotNull(Timestamp finishedSub) {
        if(finishedSub != null) {
            this.finishedSub = finishedSub;
        }
    }

    public void setStartedSubIfNotNull(Timestamp startedSub) {
        if(startedSub != null) {
            this.startedSub = startedSub;
        }
    }

    public void setAccessKeyIfNotNull(String accessKey) {
        if(accessKey != null) {
            this.accessKey = accessKey;
        }
    }

    public void setChatIdIfNotNull(Long chatId) {
        if(chatId != null) {
            this.chatId = chatId;
        }
    }

    public UserEntity(Long chatId) {
        this.chatId = chatId;
    }
}
