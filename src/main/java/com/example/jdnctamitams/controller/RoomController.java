package com.example.jdnctamitams.controller;


import com.example.jdnctamitams.dto.CancelRequestDto;
import com.example.jdnctamitams.dto.RequestDto;
import com.example.jdnctamitams.dto.ResponseDto;
import com.example.jdnctamitams.entity.Room;
import com.example.jdnctamitams.entity.RoomUseTime;
import com.example.jdnctamitams.security.UserDetailsImpl;
import com.example.jdnctamitams.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

//@CrossOrigin("http://127.0.0.1:3000,http://localhost:3000/") // 프론트엔드의 주소값을 입력해야함 127.0.0.1은 Cros가 아니니까

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3008, http://3.36.132.186:3018"}, exposedHeaders = "Authorization") // vercel서버쪽은 나중에 프론트 서버를 입력 별은 와일드 카드라 모든 오리진 cors를 다 허용해준다
@RestController
@RequiredArgsConstructor
// 파이널 필드를 사용하기 위해서 사용
@RequestMapping("/api")

public class RoomController {

    private final RoomService roomService;

    @GetMapping("/room/{floors}")
    public List<Room> getAllRoom(@PathVariable String  floors) {
        return roomService.getRoom(floors);
    }

    @GetMapping("/room-time/{id}")
    public List<RoomUseTime> getAllRoomTime(@PathVariable Long id) {
        return roomService.getRoomTime(id);
    }

    @PostMapping("/room/books")
    public ResponseDto requestDto(@RequestBody RequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse httpServletResponse) {
        return roomService.createBooks(requestDto, userDetails.getUsername(),httpServletResponse); // AuthenticationPrincipal, UserDetailsImpl
    }

    @PutMapping("/room-time/{id}")
    public ResponseDto updateRoomTime(@PathVariable Long id, @RequestBody CancelRequestDto cancelRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.updateRoomTime(id, cancelRequestDto, userDetails.getUsername());
    }

}
