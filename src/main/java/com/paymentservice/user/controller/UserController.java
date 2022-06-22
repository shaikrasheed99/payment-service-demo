package com.paymentservice.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paymentservice.response.SuccessResponse;
import com.paymentservice.user.exceptions.UserNotFoundException;
import com.paymentservice.user.model.User;
import com.paymentservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    private SuccessResponse successResponse;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers() throws JsonProcessingException {
        List<User> users = userService.getUsers();
        successResponse.addData(users);
        String response = successResponse.convertToJson();
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) throws JsonProcessingException {
        User user = userService.getUserById(id);
        if (user == null)
            throw new UserNotFoundException(List.of(Collections.singletonMap("message", "User is not found with this user id!")));
        successResponse.addData(user);
        String response = successResponse.convertToJson();
        return ResponseEntity.status(OK).body(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) throws JsonProcessingException {
        User addedUser = userService.create(user);
        successResponse.addData(Collections.singletonMap("userId", addedUser.getId()));
        String response = successResponse.convertToJson();
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<?> getBalanceById(@PathVariable UUID id) throws JsonProcessingException {
        int balance = userService.getBalanceById(id);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("userId", id);
        responseMap.put("balance", balance);
        successResponse.addData(responseMap);
        String response = successResponse.convertToJson();
        return ResponseEntity.status(OK).body(response);
    }
}
