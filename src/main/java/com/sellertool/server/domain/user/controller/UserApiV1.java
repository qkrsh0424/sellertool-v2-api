package com.sellertool.server.domain.user.controller;

import com.sellertool.server.domain.user.service.UserBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserApiV1 {
    private final UserBusinessService userBusinessService;

    @Autowired
    public UserApiV1(
            UserBusinessService userBusinessService
    ) {
        this.userBusinessService = userBusinessService;
    }

}
