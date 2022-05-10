package com.sellertool.server.config.socket;

import com.sellertool.server.config.auth.PrincipalDetails;
import com.sellertool.server.domain.exception.dto.NotAllowedAccessException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.user.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 메시지인 경우에만 인증 처리
//        if (StompCommand.CONNECT.equals(accessor.getCommand()) && accessor.getHeader("simpUser") != null && accessor.getHeader("simpUser") instanceof UsernamePasswordAuthenticationToken) {
//            UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) accessor.getHeader("simpUser");
//            PrincipalDetails principalDetails = (PrincipalDetails) userToken.getPrincipal();
//            if (principalDetails.getUser() != null) {
//                return message;
//            }
//        }
        accessor.setLeaveMutable(true);
        return message;
    }
}
