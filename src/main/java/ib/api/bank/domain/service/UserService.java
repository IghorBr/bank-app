package ib.api.bank.domain.service;

import ib.api.bank.domain.model.Account;
import ib.api.bank.domain.model.User;

import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> getLoggedUser();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByDocument(String document);

    void deleteById(Long id);
}
