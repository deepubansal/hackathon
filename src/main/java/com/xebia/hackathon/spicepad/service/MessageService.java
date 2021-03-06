package com.xebia.hackathon.spicepad.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xebia.hackathon.spicepad.dao.ChatDao;
import com.xebia.hackathon.spicepad.domain.MessageRequest;
import com.xebia.hackathon.spicepad.domain.MessageResponse;
import com.xebia.hackathon.spicepad.model.ChatMessage;
import com.xebia.hackathon.spicepad.model.FlightDate;
import com.xebia.hackathon.spicepad.model.User;

@Component
public class MessageService {

    private static Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private ChatDao chatDao;

    @Transactional
    public void addMessage(MessageRequest messageRequest) {
        Integer userId = messageRequest.getUserId();
        ChatMessage chatMessage = new ChatMessage();
        FlightDate flightDate = new FlightDate();
        Integer flightDateId = messageRequest.getFlightDateId();
        flightDate.setId(flightDateId);
        chatMessage.setFlightDate(flightDate);
        chatMessage.setMessage(messageRequest.getMessage());
        User user = new User();
        user.setId(userId);
        chatMessage.setUser(user);
        logger.info("Saving message for userId: {} and flightdateId: {}", userId, flightDateId);
        chatDao.save(chatMessage);
    }

    public List<MessageResponse> findTopMessages(Integer flightDateId) {
        List<ChatMessage> top10ByOrderByIdDesc = chatDao.findTop10ByOrderByIdDesc();
        List<MessageResponse> messageResponse = new ArrayList<>();
        for (ChatMessage chatMessage : top10ByOrderByIdDesc) {
            MessageResponse mess = new MessageResponse();
            mess.setDisplayName(chatMessage.getUser().getDisplayName());
            mess.setMessage(chatMessage.getMessage());
            messageResponse.add(mess);
        }
        return messageResponse;
    }

}
