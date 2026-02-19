package org.epst.chat;

//import jakarta.json.Json;
//import jakarta.json.JsonObject;
//import jakarta.json.JsonReader;
//import jakarta.json.JsonString;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//import org.fasterxml.jackson.core.JsonProcessingException;
//import org.fasterxml.jackson.databind.ObjectMapper;

public class MessageDecoder implements Decoder.Text<Message> {

    //private static Json obj ;

    @Override
    public Message decode(String s) {
        try {
            return (Message) JsonObject(s);
             
            //obj.createReader(s, Message.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private Message JsonObject(String s) throws JsonMappingException, JsonProcessingException {
        //JsonString jsonString = Json.createValue(s);
        //JsonObject object = jsonString.asJsonObject();
        ObjectMapper obj = new ObjectMapper();
        //
        return obj.readValue(s, Message.class);
        //new Message(object.getString("from"),object.getString("to"),object.getString("content"));
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
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