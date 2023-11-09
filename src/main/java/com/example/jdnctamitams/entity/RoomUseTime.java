package com.example.jdnctamitams.entity;

import com.example.jdnctamitams.dto.CancelRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class RoomUseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 이게 있어야 RoomUseTime에 빨간줄이 안 뜬다. 엔티티는 db에 들어가는거고 고유값인데 고유값이 달라지게 되면
    // 말이 안되기 때문에 이 키값은 db가 생성될때 하나여야 되니까

    @Column
    private boolean timeAvailable;
    @Column
    private String time;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @JsonIgnore
    @OneToOne(mappedBy = "room_use_time")
    private RoomReservationList roomReservationList;

    public void set(boolean b) {
        this.timeAvailable = b;
    }

    public void update(CancelRequestDto cancelRequestDto) {
        this.timeAvailable = cancelRequestDto.isCheck();
    }
}
