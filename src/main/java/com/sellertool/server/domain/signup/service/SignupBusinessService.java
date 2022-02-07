package com.sellertool.server.domain.signup.service;

import com.sellertool.server.domain.exception.dto.ConflictErrorException;
import com.sellertool.server.domain.exception.dto.NotAllowedAccessException;
import com.sellertool.server.domain.signup.dto.SignupDto;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.utils.DateTimeUtils;
import com.sellertool.server.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SignupBusinessService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public SignupBusinessService(
            PasswordEncoder passwordEncoder,
            UserService userService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public void signup(SignupDto signupDto) {
        String USERNAME = signupDto.getUsername();
        String PASSWORD = signupDto.getPassword();
        String PASSWORD_CHECK = signupDto.getPasswordCheck();
        String NICKNAME = signupDto.getNickname();

        if (userService.isDuplicatedUsername(USERNAME)) {
            throw new ConflictErrorException("아이디 중복 체크를 확인해 주세요.");
        }

        if (!PASSWORD.equals(PASSWORD_CHECK)) {
            throw new NotAllowedAccessException("패스워드 불일치. 잘못 된 접근 방식입니다.");
        }

        String SALT = UUID.randomUUID().toString();
        String ENC_PASSWORD = passwordEncoder.encode(PASSWORD + SALT);

        UserEntity userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .username(USERNAME)
                .password(ENC_PASSWORD)
                .nickname(NICKNAME)
                .salt(SALT)
                .roles(UserUtils.ROLE_USER)
                .allowedAccessCount(UserUtils.ALLOWED_ACCESS_COUNT_DEFAULT)
                .updatedAt(DateTimeUtils.getCurrentDateTime())
                .createdAt(DateTimeUtils.getCurrentDateTime())
                .build();

        userService.saveAndModify(userEntity);
    }

}
