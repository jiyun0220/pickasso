package com.dgsw.pickasso.exception;

import com.dgsw.pickasso.dto.ResponseDTO;  // API 응답 형태 위한 DTO
import lombok.extern.slf4j.Slf4j;  // 로깅 기능
import org.springframework.http.HttpStatus;  // HTTP 상태코드
import org.springframework.http.ResponseEntity;  // 응답 전체 포장 역할
import org.springframework.validation.BindingResult;  // 검증 결과 처리
import org.springframework.validation.FieldError;  // 필드별 유효성 검증 오류
import org.springframework.web.bind.MethodArgumentNotValidException;  // 유효성 검증 예외
import org.springframework.web.bind.annotation.ExceptionHandler;  // 예외처리 어노테이션
import org.springframework.web.bind.annotation.RestControllerAdvice;  // 모든 컨트롤러에 적용되는 예외처리


import java.util.HashMap;  // 해시맵 사용
import java.util.Map;  // 맵 인터페이스 사용

@Slf4j  // 로깅 기능 활성화
@RestControllerAdvice  // 전역 예외처리를 위한 어노테이션
public class GlobalExceptionHandler {  // 모든 컨트롤러의 예외를 중앙에서 처리하는 클래스

    // 투표가 없을 때 예외 처리
    @ExceptionHandler(VoteNotFoundException.class)  // 이 예외 타입을 처리하는 메소드로 지정
    public ResponseEntity<ResponseDTO<String>> handleVoteNotFoundException(VoteNotFoundException e) {
        log.error("투표를 찾을 수 없습니다: {}", e.getMessage());  // 오류 로깅
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)  // 404 오류 상태코드 설정
                .body(new ResponseDTO<>(false, e.getMessage(), null));  // 오류 메시지를 포함한 응답 반환
    }

    // 옵션이 없을 때 예외 처리
    @ExceptionHandler(OptionNotFoundException.class)  // 이 예외 타입을 처리하는 메소드로 지정
    public ResponseEntity<ResponseDTO<String>> handleOptionNotFoundException(OptionNotFoundException e) {
        log.error("옵션을 찾을 수 없습니다: {}", e.getMessage());  // 오류 로깅
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)  // 404 오류 상태코드 설정
                .body(new ResponseDTO<>(false, e.getMessage(), null));  // 오류 메시지를 포함한 응답 반환
    }

    // 유효하지 않은 투표 작업 예외 처리
    @ExceptionHandler(InvalidVoteOperationException.class)  // 이 예외 타입을 처리하는 메소드로 지정
    public ResponseEntity<ResponseDTO<String>> handleInvalidVoteOperationException(InvalidVoteOperationException e) {
        log.error("유효하지 않은 투표 작업: {}", e.getMessage());  // 오류 로깅
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)  // 400 잘못된 요청 오류 상태코드
                .body(new ResponseDTO<>(false, e.getMessage(), null));  // 오류 메시지를 포함한 응답 반환
    }

    // 입력 값 검증 실패 예외 처리 (DTO 검증 오류)
    @ExceptionHandler(MethodArgumentNotValidException.class)  // 이 예외 타입을 처리하는 메소드로 지정
    public ResponseEntity<ResponseDTO<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        BindingResult result = ex.getBindingResult();  // 검증 결과 가져오기
        Map<String, String> errors = new HashMap<>();  // 오류 내용을 담을 변수
        
        // 각 필드의 유효성 검증 오류 처리
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());  // 필드명과 오류메세지 저장
        }
        
        log.error("입력값 검증 오류: {}", errors);  // 오류 로깅
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)  // 400 잘못된 요청 오류 상태코드
                .body(new ResponseDTO<>(false, "입력값 검증 오류", errors));  // 오류 내용을 포함한 응답 반환
    }

    // 다른 모든 예외를 처리하는 멤버 - 일반적인 서버 오류
    @ExceptionHandler(Exception.class)  // 다른 모든 예외 타입을 처리하는 일반 예외처리
    public ResponseEntity<ResponseDTO<String>> handleGlobalException(Exception e) {
        log.error("서버 오류 발생: {}", e.getMessage(), e);  // 오류와 스택 트레이스 로깅
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)  // 500 서버 오류 상태코드
                .body(new ResponseDTO<>(false, "서버 내부 오류가 발생했습니다", null));  // 일반 오류 메시지 반환
    }
}
