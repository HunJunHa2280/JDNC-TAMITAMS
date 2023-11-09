package com.example.jdnctamitams.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "Users")
public class Users {
// DB가 계속 생성되는 이유는 조작을 못하고 있어서 새롭게 생성되고 있는건데 어떻게 할지 매니저님에게 물어보기

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

}