package com.example.jdnctamitams.repository;


import com.example.jdnctamitams.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByFloors(String floors);

    Room findByRoomName(String roomName);
}