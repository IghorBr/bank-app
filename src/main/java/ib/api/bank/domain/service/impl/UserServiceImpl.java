package ib.api.bank.domain.service.impl;

import ib.api.bank.domain.model.Account;
import ib.api.bank.domain.model.User;
import ib.api.bank.domain.repository.UserRepository;
import ib.api.bank.domain.service.AccountService;
import ib.api.bank.domain.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> getLoggedUser() {
        Long userId = 1L;
        return this.userRepository.findById(userId);
    }

    @Override
    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByDocument(String document) {
        return this.userRepository.findByDocument(document);
    }

    @Override
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }
}
