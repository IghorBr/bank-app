package ib.api.bank.api.controller;

import ib.api.bank.api.dto.in.AccountIn;
import ib.api.bank.api.dto.out.AccountOut;
import ib.api.bank.api.mapper.AccountMapper;
import ib.api.bank.api.mapper.UserMapper;
import ib.api.bank.domain.model.Account;
import ib.api.bank.domain.model.User;
import ib.api.bank.domain.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper;

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountOut> findAccountByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        return this.accountService.findByAccountNumber(accountNumber)
                .map(this.accountMapper.toOut())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody @Valid AccountIn in) {
        User user = this.userMapper.toDomain().apply(in.userIn());
        Account account = this.accountMapper.toDomain().apply(in);



        return ResponseEntity.ok().build();
    }
}
