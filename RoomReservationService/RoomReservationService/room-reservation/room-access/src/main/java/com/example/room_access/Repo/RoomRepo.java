package com.example.room_access.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.room_access.Models.Room;

public interface RoomRepo extends JpaRepository<Room,Long>{

    public Room save(Room room);

}
