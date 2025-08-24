package ib.api.bank.core.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class TokenService {

    private final Algorithm algorithm;

    public TokenService(@Value("${token.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String tokenize(Long userId) throws TokenException {
        try {
            return JWT.create()
                    .withJWTId(String.valueOf(userId))
                    .sign(algorithm);
        } catch (IllegalArgumentException | JWTCreationException e) {
            throw new TokenException("Erro ao gerar JWT", e);
        }
    }

    public Long load(String jwt) throws TokenException {
        try {
            var verification = JWT.require(algorithm);
            var verifier = verification.build();
            DecodedJWT decoded = verifier.verify(jwt);
            return Long.valueOf(decoded.getId());
        } catch (Exception e) {
            throw new TokenException("Erro ao decodificar JWT", e);
        }
    }
}
