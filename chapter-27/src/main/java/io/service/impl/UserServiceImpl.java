package io.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.domain.entity.Account;
import io.repo.UserRepository;
import io.service.UserService;

@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional 
    @Override
    public void createUser(Account account) {
        userRepository.save(account);
    } 
}
