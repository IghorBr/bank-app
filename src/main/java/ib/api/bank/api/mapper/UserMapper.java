package ib.api.bank.api.mapper;

import ib.api.bank.api.dto.in.UserIn;
import ib.api.bank.api.dto.out.UserOut;
import ib.api.bank.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserMapper {

    public Function<User, UserOut> toOut() {
        return user -> new UserOut(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getDocument()
        );
    }

    public Function<UserIn, User> toDomain() {
        return userIn -> new User(
                userIn.name(),
                userIn.email(),
                userIn.document()
        );
    }
}
