package herve.com.websocket10.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    private String sender;
    private String content;
    private String type;  //CHAT,JOIN,LEAVE
}
