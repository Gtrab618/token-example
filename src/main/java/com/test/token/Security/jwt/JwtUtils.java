package com.test.token.Security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jtw.time.expiration}")
    private String timeExpiration;

    //Generar token de accceso
    public String generateAccesToken(String username, String role){

        return Jwts.builder()
                .claim("rol",role)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignatureKey()).compact();
    }

    //Obtener firma del token
    public SecretKey getSignatureKey (){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //validar token correcto

    public boolean isTokenValid(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSignatureKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return true;
        }catch (Exception e){
            log.error("token invalido" .concat(e.getMessage()));
            return false;
        }
    }

    //obtener el usarname
    public String getUsernameFormToken(String token){
        return getClaim(token,Claims::getSubject);
    }

    //obtener el rol
    public String getRoleFromToken(String token) {
        return getClaim(token, claims -> claims.get("rol", String.class));
    }

    //obtener 1 solo cliams
    public <T> T getClaim(String token, Function<Claims,T> claimsTFunction){
        Claims claims= extractAllClaim(token);
        return claimsTFunction.apply(claims);
    }

    //obtener todos los claims del token
    public Claims extractAllClaim(String token){
        return Jwts.parser()
                .verifyWith(getSignatureKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
