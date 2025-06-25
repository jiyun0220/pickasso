package com.dgsw.pickasso.exception;

// 투표 작업 중에 발생하는 유효하지 않은 작업 예외
public class InvalidVoteOperationException extends RuntimeException {  // RuntimeException을 상속하여 예외로 처리
    // 오류 메시지를 매개변수로 받는 생성자
    public InvalidVoteOperationException(String message) {
        super(message);  // 부모 클래스 생성자에 메시지 전달
    }
}
