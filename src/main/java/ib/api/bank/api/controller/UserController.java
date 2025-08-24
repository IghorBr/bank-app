package ib.api.bank.api.controller;

import ib.api.bank.api.dto.in.UserIn;
import ib.api.bank.api.dto.out.UserOut;
import ib.api.bank.api.mapper.AccountMapper;
import ib.api.bank.api.mapper.UserMapper;
import ib.api.bank.domain.service.AccountService;
import ib.api.bank.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<UserOut> getLoggedUser() {
        return this.userService.getLoggedUser()
                .map(this.mapper.toOut())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
