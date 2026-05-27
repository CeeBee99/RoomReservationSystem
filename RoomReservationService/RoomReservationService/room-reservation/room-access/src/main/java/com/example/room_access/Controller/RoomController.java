package com.example.room_access.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.room_access.Models.Room;
import com.example.room_access.Service.RoomAccessService;

@RestController
@RequestMapping("/internal/rooms")
public class RoomController {
    @Autowired
    private RoomAccessService service;

    @PostMapping
    public Room create(@RequestBody Room room){
        return service.save(room);
    }

    @GetMapping
    public List<Room> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Room findbyID(@PathVariable Long id) {
        return service.findById(id).orElseThrow();
    }
    

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id){
        service.delete(id);
    }

    @PatchMapping("/{id}/status")
    public Room update(@PathVariable Long id, @RequestBody String status){
        Room room = service.findById(id).orElseThrow();
        room.setStatus(status);
        return service.save(room);
    }
    @PatchMapping("/{id}/features")
    public Room addFeatures(@PathVariable Long id, @RequestBody String features){
        Room room = service.findById(id).orElseThrow();
        room.getFeatures().add(features);
        return service.save(room);
    }
    
    @DeleteMapping("/{id}/features")
    public Room deleteFeatures(@PathVariable Long id, @RequestBody String features){
        Room room = service.findById(id).orElseThrow();
        room.getFeatures().remove(features);
        return service.save(room);
    }
}
