package com.example.jdnctamitams.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseDto {

    private String msg;

    public ResponseDto(String s) {
        this.msg = s;
    }
    // 다른 사람이 봐도 알 수 있게끔 String에 s만 해놓지 말고 제대로 알 수 있게 이름을 하자
}