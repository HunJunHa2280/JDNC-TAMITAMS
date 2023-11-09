package com.example.jdnctamitams.service;

import com.example.jdnctamitams.dto.CancelRequestDto;
import com.example.jdnctamitams.dto.RequestDto;
import com.example.jdnctamitams.dto.ResponseDto;
import com.example.jdnctamitams.entity.Room;
import com.example.jdnctamitams.entity.RoomReservationList;
import com.example.jdnctamitams.entity.RoomUseTime;
import com.example.jdnctamitams.entity.Users;
import com.example.jdnctamitams.repository.RoomRepository;
import com.example.jdnctamitams.repository.RoomReservationListRepository;
import com.example.jdnctamitams.repository.RoomUseTimeRepository;
import com.example.jdnctamitams.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomUseTimeRepository useTimeRepository;
    private final RoomReservationListRepository roomReservationListRepository;
    private final UsersRepository usersRepository;

    @Transactional // 이 메서드 안에서 에러가 터진 라인은 수행이 된건데 수행이 안된걸로 취급해주고 롤백을 해준다.
    // 방금 에러가 터졌는데 요청이 수행이 됐으니까 이걸 방지해주는게 이 어노테이션이다. 이 어노테이션이 작동 원리가 면접에서 자주 나온다.
    // 작동 원리를 공부하려면 지금 하지말고 수료하고 하자
    public ResponseDto createBooks(RequestDto requestDto, String username, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("access-control-allow-origin", "*");
        Room room = roomRepository.findByRoomName(requestDto.getRoomName());

        RoomUseTime useTime = useTimeRepository.findByRoomAndTime(room, requestDto.getTime());

        useTime.set(!requestDto.isCheck());
        useTimeRepository.save(useTime);
        RoomReservationList roomReservationList = new RoomReservationList();
        String name = username;
        roomReservationList.gain(name, useTime.getRoom().getRoomName(), useTime);
        ResponseDto responseDto = new ResponseDto("예약이 완료되었습니다.");
        roomReservationListRepository.save(roomReservationList);
        return responseDto;
        //최종 예약하기
    }
    @Transactional
    public List<Room> getRoom(String floors) {
        List<Room> roomList = roomRepository.findAllByFloors(floors);
        return roomList;
        // 층 조회
    }

    @Transactional
    public List<RoomUseTime> getRoomTime(Long id) {
        Room room = roomRepository.findById(id).orElseThrow( ()
            -> new NullPointerException("해당 접근은 잘못된 접근입니다."));
        List<RoomUseTime> roomUseTimeList = useTimeRepository.findAllByRoom(room);
        return roomUseTimeList;
        // 개별 조회
    }

    @Transactional
    public ResponseDto updateRoomTime(Long id, CancelRequestDto cancelRequestDto, String username) {
        // 찾아 오기
        RoomUseTime roomUseTime = useTimeRepository.findById(id).orElseThrow(()->
           new NullPointerException("해당 접근은 잘못된 접근입니다."));
            // 수정 하기
            roomUseTime.update(cancelRequestDto);
            // 수정된 정보를 DB에 저장 하기
            useTimeRepository.save(roomUseTime);

        RoomReservationList roomReservationList = new RoomReservationList();
        Users users = usersRepository.findByName(username).orElseThrow(()
                -> new NullPointerException("해당 인원은 없습니다."));

        String name = users.getName();
            roomReservationList.abc(roomUseTime, name,roomUseTime.getRoom().getRoomName()); // 예약어 피하려고 그냥 대충 abc라 붙임
            roomReservationListRepository.save(roomReservationList);
            return new ResponseDto("수정을 완료되었습니다.");
    }
}