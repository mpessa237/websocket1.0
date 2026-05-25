package herve.com.websocket10.controllers;

import herve.com.websocket10.models.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
