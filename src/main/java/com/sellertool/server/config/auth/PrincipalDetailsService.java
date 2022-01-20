package com.sellertool.server.config.auth;

import java.util.Optional;

import com.sellertool.server.domain.user.model.entity.UserEntity;
import com.sellertool.server.domain.user.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        
        if(userOpt.isPresent()) {
            return new PrincipalDetails(userOpt.get());
        } else {
            throw new UsernameNotFoundException("email : " + email + ", not found.");
        }
    }
}
