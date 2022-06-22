package com.paymentservice.user.service;

import com.paymentservice.bank.service.BankService;
import com.paymentservice.user.exceptions.UserNotFoundException;
import com.paymentservice.user.model.User;
import com.paymentservice.user.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private BankService bankService;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(UUID id) {
        User user = null;
        try {
            user = userRepository.findById(id).get();
        } catch (Exception exception) {
            user = null;
        }
        return user;
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public int getBalanceById(UUID userId) {
        User user = getUserById(userId);
        if (!isUserExist(user))
            throw new UserNotFoundException(List.of(Collections.singletonMap("message", "User is not found with this user id!")));

        return bankService.balance(userId);
    }

    private boolean isUserExist(User user) {
        return user != null;
    }
}
