package com.sellertool.server.domain.user.service;

import com.sellertool.server.domain.user.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserBusinessService {
    private final UserService userService;
    @Autowired
    public UserBusinessService(
            UserService userService
    ){
        this.userService = userService;
    }
}
