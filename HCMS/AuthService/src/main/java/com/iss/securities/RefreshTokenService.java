package com.iss.securities;

import com.iss.models.RefreshTokenEntity;
import com.iss.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService
{
    private final RefreshTokenRepository refreshTokenRepository;
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 days

    public RefreshTokenEntity createRefreshToken(String username)
    {
        refreshTokenRepository.deleteByUsername(username); // remove old

        RefreshTokenEntity token = RefreshTokenEntity.builder()
                .username(username)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(REFRESH_TOKEN_VALIDITY))
                .build();

        return refreshTokenRepository.save(token);
    }

    public Optional<RefreshTokenEntity> validateRefreshToken(String token)
    {
        return refreshTokenRepository.findByToken(token)
                .filter(rt -> rt.getExpiryDate().isAfter(Instant.now()));
    }

    public Optional<RefreshTokenEntity> rotateRefreshToken(String oldToken)
    {
        return validateRefreshToken(oldToken)
                .map(rt -> {
                    refreshTokenRepository.delete(rt);
                    return createRefreshToken(rt.getUsername());
                });
    }

    public void deleteByUsername(String username)
    {
        refreshTokenRepository.deleteByUsername(username);
    }
}
