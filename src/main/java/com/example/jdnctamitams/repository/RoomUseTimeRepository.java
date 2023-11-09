package com.example.jdnctamitams.repository;

import com.example.jdnctamitams.entity.Room;
import com.example.jdnctamitams.entity.RoomUseTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomUseTimeRepository extends JpaRepository<RoomUseTime, Long> {

    List<RoomUseTime> findAllByRoom(Room room);

    RoomUseTime findByRoomAndTime(Room room, String time);
}
