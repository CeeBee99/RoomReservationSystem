package com.example.room_manager.Client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.room_manager.Model.Room;

@Component
public class RoomAccessClient {
    
    @Autowired
    private RestTemplate rest;

    private final String URL = "http://room-access:8083/internal/rooms";

    public List<Room> getAll(){
        return Arrays.asList(
            rest.getForObject(URL, Room[].class)
        );
    }

    public Room create(Room room){
        return  rest.postForObject(URL, room, Room.class);
    }

    public Room getById(Long id){
        return rest.getForObject(URL + "/" + id, Room.class);
    }

    public void delete(Long id){
        rest.delete(URL + "/" + id);
    }
}
