package com.sellertool.server.domain.user.service;

import com.sellertool.server.config.auth.PrincipalDetails;
import com.sellertool.server.domain.exception.dto.InvalidUserAuthException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public void saveAndModify(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public boolean isLogin() {
        if (SecurityContextHolder.getContext().getAuthentication().getName() != null && SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            return false;
        }
        return true;
    }

    public UUID getUserId() {
        try {
            PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            return principalDetails.getUser().getId();
        } catch (Exception e) {
            throw new InvalidUserAuthException("로그인 회원이 아니거나, 잘못된 접근방식임.");
        }
    }

    public UserEntity searchUserByUserId(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean isDuplicatedUsername(String username) {
        if (username == null) {
            throw new NotMatchedFormatException("아이디 정보를 입력해 주세요.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);

        List<UserEntity> userEntities = userRepository.qSelectList(params);

        return userEntities.stream().findAny().isPresent();
    }

    public boolean isDuplicatedEmail(String email) {
        if (email == null) {
            throw new NotMatchedFormatException("이메일 정보를 입력해 주세요.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        List<UserEntity> userEntities = userRepository.qSelectList(params);

        return userEntities.stream().findAny().isPresent();
    }

    public boolean isDuplicatedNickname(String nickname) {
        if (nickname == null) {
            throw new NotMatchedFormatException("닉네임 정보를 입력해 주세요.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("nickname", nickname);

        List<UserEntity> userEntities = userRepository.qSelectList(params);

        return userEntities.stream().findAny().isPresent();
    }
}
