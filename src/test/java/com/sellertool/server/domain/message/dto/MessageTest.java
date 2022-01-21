package com.sellertool.server.domain.message.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

import com.sellertool.server.domain.message.model.dto.Message;

class MessageTest {
    @Test
    public void messageGetTest(){
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData("hello");

        Assertions.assertThat(message.getStatus()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(message.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(message.getStatusMessage()).isEqualTo(HttpStatus.OK.name());

        System.out.println(message);
    }
}