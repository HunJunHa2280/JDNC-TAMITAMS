package com.example.jdnctamitams.controller;

import com.example.jdnctamitams.dto.SignupRequestDto;
import com.example.jdnctamitams.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3008, http://3.36.132.186:3018"}, exposedHeaders = "Authorization") // vercel서버쪽은 나중에 프론트 서버를 입력 별은 와일드 카드라 모든 오리진 cors를 다 허용해준다
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/auth/signup")
    // 회원가입 하는 코드
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                return new ResponseEntity<>("상태코드 : " + HttpStatus.BAD_REQUEST.value() + ", 메세지 : " + fieldError.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return usersService.signup(requestDto);
    }


}