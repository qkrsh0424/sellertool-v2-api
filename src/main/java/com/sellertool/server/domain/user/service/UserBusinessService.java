package com.sellertool.server.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserBusinessService {
    private final UserService userService;
    @Autowired
    public UserBusinessService(
            UserService userService
    ){
        this.userService = userService;
    }
    public Object checkUsernameDuplicate(Map<String, Object> params){
        Object usernameObj = params.get("username");
        Map<String, Object> resultData = new HashMap<>();

        if (usernameObj == null) {
            resultData.put("isEmpty", true);
            resultData.put("isDuplicated", false);
        }
        String USERNAME = usernameObj.toString();

        if(!userService.isDuplicatedUsername(USERNAME)){
            resultData.put("isEmpty", false);
            resultData.put("isDuplicated", false);
        }else{
            resultData.put("isEmpty", false);
            resultData.put("isDuplicated", true);
        }
        return resultData;
    }
}
