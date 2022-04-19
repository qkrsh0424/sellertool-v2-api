package com.sellertool.server.domain.invite_member.service;

import com.sellertool.server.domain.invite_member.entity.InviteMemberEntity;
import com.sellertool.server.domain.invite_member.repository.InviteMemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class InviteMemberBusinessServiceTest {
//    @Autowired
//    InviteMemberRepository inviteMemberRepository;
//
//    @Test
//    public void createTest(){
//        for(int i = 0 ; i < 1000; i++){
//            InviteMemberEntity entity = InviteMemberEntity.builder()
//                    .id(UUID.randomUUID())
//                    .workspaceId(UUID.fromString("a1fac39c-8fdf-4977-8801-9148c8fc9efd"))
//                    .userId(UUID.fromString("0ef69e04-6950-4ffb-9fbc-ce0ab59b9cd8"))
//                    .build();
//            inviteMemberRepository.save(entity);
//        }
//        for(int i = 0 ; i < 1000; i++){
//            InviteMemberEntity entity = InviteMemberEntity.builder()
//                    .id(UUID.randomUUID())
//                    .workspaceId(UUID.fromString("afd8f25e-1f05-4750-b161-e1d9c1ecbc9a"))
//                    .userId(UUID.fromString("0ef69e04-6950-4ffb-9fbc-ce0ab59b9cd8"))
//                    .build();
//            inviteMemberRepository.save(entity);
//        }
//    }
}