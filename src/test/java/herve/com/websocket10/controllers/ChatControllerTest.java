package herve.com.websocket10.controllers;

import herve.com.websocket10.models.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    private final ChatController chatController = new ChatController();

    @Test
    void sendMessage_doitRetournerLeMemeMessage(){

        // --Arrange

        Message message = new Message();
        message.setSender("Herve");
        message.setContent("Hello world");
        message.setType("CHAT");

        // --Act
        Message result = chatController.sendMessage(message);

        // --Assert
        assertNotNull(result);
        assertEquals("Herve",result.getSender());
        assertEquals("Hello world",result.getContent());
        assertEquals("CHAT",result.getType());
    }

    @Test
    void sendMessage_avecMessageVide_doitRetournerMessageVide(){

        //--arrange
        Message message = new Message();
        message.setSender("Dojo");
        message.setContent("");
        message.setType("CHAT");

        //--act
        Message result = chatController.sendMessage(message);

        //--assert
        assertNotNull(result);
        assertEquals("Dojo",result.getSender());
        assertEquals("",result.getContent());

    }

    @Test
    void addUser_doitSauvegarderUsernameInSession(){

        // --arrange
        Message message = new Message();
        message.setSender("herve");
        message.setContent("herve a rejoint le chat");
        message.setType("JOIN");

        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);

        Map<String,Object> sessionAttributes = new HashMap<>();

        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);

        Message result = chatController.addUser(message,headerAccessor);

        assertNotNull(result);
        assertEquals("herve",result.getSender());
        assertEquals("JOIN",result.getType());
        assertEquals("herve",sessionAttributes.get("username"));

        verify(headerAccessor,times(1)).getSessionAttributes();
    }

    @Test
    void addUser_avecDifferentsUtilisateurs_doitSauvegarderLeBonUsername(){

        // --arrange

        Message message = new Message();
        message.setSender("Bob");
        message.setType("JOIN");

        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        Map<String, Object> sessionAttributes = new HashMap<>();
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);

        // -- act
        chatController.addUser(message,headerAccessor);

        // --assert
        assertEquals("Bob",sessionAttributes.get("username"));
    }
}
