package com.study.security.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.security.domain.PersistentLogins;
import com.study.security.repository.RememberMeTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RememberMeTokenService implements PersistentTokenRepository {

    private final RememberMeTokenRepository rememberMeTokenRepository;
    
    @Override
    @Transactional
    public void createNewToken(PersistentRememberMeToken token) {
        LocalDateTime tokenDate = token.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        PersistentLogins persistentLogins 
            = new PersistentLogins(token.getUsername(), token.getSeries(), token.getTokenValue(), tokenDate);
        rememberMeTokenRepository.save(persistentLogins);
        
    }

    @Override
    @Transactional
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        LocalDateTime lastUsedTime = lastUsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        rememberMeTokenRepository.updateTokenBySeries(tokenValue, lastUsedTime, series);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Optional<PersistentLogins> opt = rememberMeTokenRepository.findBySeries(seriesId);
        
        if (opt.isEmpty()) {
            return null;
        }
        
        return opt.filter(p -> !Objects.isNull(p))
                  .map(p -> new PersistentRememberMeToken(p.getUsername(), 
                                                          p.getSeries(), 
                                                          p.getToken(),
                                                          Date.from(p.getLastUsedTime()
                                                              .atZone(ZoneId.systemDefault())
                                                              .toInstant())
                                                          ))
                                                          .get();
    }
    
    @Override
    public void removeUserTokens(String username) {
        rememberMeTokenRepository.deleteById(username);
    }
   
}
