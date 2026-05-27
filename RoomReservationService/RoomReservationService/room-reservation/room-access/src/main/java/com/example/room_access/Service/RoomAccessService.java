package com.example.room_access.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.room_access.Models.Room;
import com.example.room_access.Repo.RoomRepo;

@Service
public class RoomAccessService {
    @Autowired
    private  RoomRepo repo;

    public Room save(Room room){
        return repo.save(room);
    }

    public List<Room> findAll(){
        return repo.findAll();
    }

    public Optional<Room> findById(Long id){
        return repo.findById(id);
    }

    public void delete(Long id){
        repo.deleteById(id);
    }
}
