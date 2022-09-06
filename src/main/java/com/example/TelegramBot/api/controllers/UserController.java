package com.example.TelegramBot.api.controllers;

import com.example.TelegramBot.api.entities.UserEntity;
import com.example.TelegramBot.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
//@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String GET_USERS = "/user/all";
    private static final String CREATE_USER = "/user";

    private static final String UPDATE_OR_DELETE_USER = "/user/{chatId}";
    private static final String UPDATE_DATE_USER = "/user/{chatId}/date";

    @GetMapping(GET_USERS)
    @Operation(summary = "Вывод всех пользователей")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping(CREATE_USER)
    @Operation(summary = "Добавление записи о пользователе")
    public ResponseEntity saveUser(
            @RequestParam("chatId") @Parameter(description = "Чат id пользователя") Long chatId,
            @RequestParam("firstName") @Parameter(description = "Имя пользователя") String firstName,
            @RequestParam("lastName") @Parameter(description = "Фамилия пользователя") String lastName,
            @RequestParam("userName") @Parameter(description = "Никнейм пользователя") String userName,
            @RequestParam("registeredAt") @Parameter(description = "Дата регистрации пользователя. Формат: yyyy-MM-dd") String registeredAt,
            @RequestParam("startedSub") @Parameter(description = "Дата начала действия подписки пользователя. Формат: yyyy-MM-dd") String startedSub,
            @RequestParam("finishedSub") @Parameter(description = "Дата окончания действия подписки пользователя. Формат: yyyy-MM-dd") String finishedSub,
            @RequestParam("freeSub") @Parameter(description = "Была ли у пользователя пробный период действия подписки") boolean freeSub,
            @RequestParam("accessKey") @Parameter(description = "Ключ доступа") String accessKey,
            @RequestParam("idKey") @Parameter(description = "Id ключ") int idKey,
            @RequestParam("referralUsers") @Parameter(description = "Количество приглашенных пользователей по реферальной ссылке") int referralUsers
    ) {
        if (userService.existsById(chatId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Пользователь с данным id уже существует");
        }
        try {
            Date parsedDate = dateFormat.parse(registeredAt);
            Date parsedDate1 = dateFormat.parse(startedSub);
            Date parsedDate2 = dateFormat.parse(finishedSub);
            Timestamp registered = new java.sql.Timestamp(parsedDate.getTime());
            Timestamp started = new java.sql.Timestamp(parsedDate1.getTime());
            Timestamp finished = new java.sql.Timestamp(parsedDate2.getTime());
            UserEntity newUser = new UserEntity(chatId, firstName, lastName, userName, registered, started, finished, freeSub, accessKey, idKey, referralUsers);
            userService.addUser(newUser);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format(ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Пользователь добавлен");
    }


    @PatchMapping(UPDATE_OR_DELETE_USER)
    @Operation(summary = "Обновление записи о пользователе по переданному id", description = "Позволяет обновить запись по полученному из ссылки id. При оставлении пустых полей изменения этих данных не произойдет")
    public ResponseEntity updateUser(
            @PathVariable Long chatId,
            @RequestParam(name = "firstName", required = false) @Parameter(description = "Имя пользователя") String firstName,
            @RequestParam(name = "lastName", required = false) @Parameter(description = "Фамилия пользователя") String lastName,
            @RequestParam(name = "userName", required = false) @Parameter(description = "Никнейм пользователя") String userName,
            @RequestParam(name = "registeredAt", required = false) @Parameter(description = "Дата регистрации пользователя. Формат: yyyy-MM-dd") String registeredAt,
            @RequestParam(name = "startedSub", required = false) @Parameter(description = "Дата начала действия подписки пользователя. Формат: yyyy-MM-dd") String startedSub,
            @RequestParam(name = "finishedSub", required = false) @Parameter(description = "Дата окончания действия подписки пользователя. Формат: yyyy-MM-dd") String finishedSub,
            @RequestParam(name = "freeSub", required = false) @Parameter(description = "Была ли у пользователя пробный период действия подписки") Optional<Boolean> freeSub,
            @RequestParam(name = "accessKey", required = false) @Parameter(description = "Ключ доступа") String accessKey,
            @RequestParam(name = "idKey", required = false) @Parameter(description = "Id ключ") Optional<Integer> idKey,
            @RequestParam(name = "referralUsers", required = false) @Parameter(description = "Количество приглашенных пользователей по реферальной ссылке") Optional<Integer> referralUsers) {
        UserEntity user = userService.findById(chatId);
        if (user == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Пользователь не найден");
        }
        try {
            if (registeredAt != null)
            {
                Date parsedDate = dateFormat.parse(registeredAt);
                Timestamp registered = new java.sql.Timestamp(parsedDate.getTime());
                user.setRegisteredAt(registered);
            }
            if (startedSub != null) {
                Date parsedDate1 = dateFormat.parse(startedSub);
                Timestamp started = new java.sql.Timestamp(parsedDate1.getTime());
                user.setStartedSubIfNotNull(started);
            }
            if (finishedSub != null) {
                Date parsedDate2 = dateFormat.parse(finishedSub);
                Timestamp finished = new java.sql.Timestamp(parsedDate2.getTime());
                user.setFinishedSubIfNotNull(finished);
            }
            user.setFirstNameIfNotNull(firstName);
            user.setLastNameIfNotNull(lastName);
            user.setUserNameIfNotNull(userName);
            user.setAccessKeyIfNotNull(accessKey);

            if (freeSub.isPresent()){
                user.setFreeSub(freeSub.get());
            }
            if (idKey.isPresent()) {
                user.setIdKey(idKey.get());
            }
            if (idKey.isPresent()) {
                user.setReferralUsers(referralUsers.get());
            }
            userService.updateUser(chatId, user);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format(ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Данные о пользователе обновлены");
    }

    @DeleteMapping(UPDATE_OR_DELETE_USER)
    @Operation(summary = "Удаление записи о пользователе по переданному id", description = "Позволяет удалить запись по id")
    public ResponseEntity deleteUser (@PathVariable @Parameter(description = "чат id пользователя") Long chatId) {
        try {
            userService.deleteUser(chatId);
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format(ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(String.format("Пользователь " + chatId + " удален"));
    }

    @PatchMapping(UPDATE_DATE_USER)
    @Operation(summary = "Обновление даты окончания действия подписки пользователя по переданному id", description = "Позволяет обновить запись по полученному из ссылки id")
    public ResponseEntity updateUserDate(
            @PathVariable Long chatId,
            @RequestParam(name = "finishedSub", required = false) @Parameter(description = "Дата окончания действия подписки пользователя. Формат: yyyy-MM-dd") String finishedSub) throws ParseException {
        if (!userService.existsById(chatId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Пользователь не найден");
        }
        try {
            Date parsedDate2 = dateFormat.parse(finishedSub);
            Timestamp finished = new java.sql.Timestamp(parsedDate2.getTime());
            userService.updateUserDate(chatId, finished);
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("Неверный формат данных " + ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Данные о пользователе обновлены");
    }
}
