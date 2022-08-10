package org.epst.chat;

//import javax.json.Json;
//import javax.json.JsonArrayBuilder;
//import javax.json.JsonObjectBuilder;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

//import org.fasterxml.jackson.core.JsonProcessingException;
//import org.fasterxml.jackson.databind.ObjectMapper;

public class MessageEncoder implements Encoder.Text<Message> {

    //private static ObjectMapper obj = new ObjectMapper();

    @Override
    public String encode(Message message) {
        try {
            ObjectMapper obj = new ObjectMapper();
            
            return obj.writeValueAsString(message);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }

}