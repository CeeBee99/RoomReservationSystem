package com.example.room_manager.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.room_manager.Model.Room;
import com.example.room_manager.Service.RoomManagerService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/rooms")
public class RoomManagerController {
    @Autowired
    private RoomManagerService service;

    @GetMapping
    public List<Room> getAll() {
        return service.getAllRooms();
    }

    @PostMapping
    public Room create(@RequestBody Room room) {
        return service.createRoom(room);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.deleteRoom(id);
    }
    
    
}
