package ru.yandex.practicum.catsgram.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exceptions.InvalidEmailException;
import ru.yandex.practicum.catsgram.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.catsgram.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private Map<String,User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(PostController.class);
    @GetMapping("/users")
    public Map<String,User> getUser() {
        return users;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        if(user.getEmail() == null | user.getEmail().isBlank() | user.getEmail().isEmpty()) {
            log.debug("Отсутствует email");
            throw new InvalidEmailException("Отсутствует email");
        }

        if(users.containsKey(user.getEmail())) {
            log.debug("Такой пользователь уже есть!");
           throw new UserAlreadyExistException("Такой пользователь уже есть!");
        }

        users.put(user.getEmail(), user);
        log.debug("Добавлен пользователь " +user);
        return users.get(user.getEmail());
    }

    @PutMapping("/users")
    public User upUserOrCreateUser(@RequestBody User user) {
        if(user.getEmail() == null | user.getEmail().isBlank() | user.getEmail().isEmpty()) {
            log.debug("Отсутствует email");
            throw new InvalidEmailException("Отсутствует email");
        }

        if(users.containsKey(user.getEmail())) {
        User user1 = new User(user.getEmail()
                , user.getNickname()
                ,user.getBirthdate());
              //  , user.getBirthdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        users.remove(user.getEmail());
        users.put(user.getEmail(),user1);
            log.debug("Обновлен пользователь " +user1);
        } else {
            users.put(user.getEmail(), user);
            log.debug("Добавлен пользователь " +user);
        }
        return users.get(user.getEmail());
    }

}
