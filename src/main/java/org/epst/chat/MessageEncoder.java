package org.epst.chat;

//import jakarta.json.Json;
//import jakarta.json.JsonArrayBuilder;
//import jakarta.json.JsonObjectBuilder;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

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