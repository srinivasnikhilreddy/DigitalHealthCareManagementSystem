package com.iss.securities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void init()
    {
        System.out.println("JWT Secret = " + secret);
    }

    private static final String ISSUER = "JOB_TRACKER_APP";

    public String generateAccessToken(String username, Set<String> roles) throws IllegalArgumentException, JWTCreationException
    {
        //System.out.println(username+","+roles);
        return JWT.create()
                .withSubject("User Details")
                .withClaim("username", username)
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withIssuedAt(new Date())
                .withIssuer(ISSUER)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException
    {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer(ISSUER)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    public String[] getRolesFromToken(String token)
    {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(ISSUER)
                .build()
                .verify(token);

        return jwt.getClaim("roles").asArray(String.class);
    }
}
