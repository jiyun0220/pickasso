package com.dgsw.pickasso.exception;  // 예외 처리 클래스들을 모아놓은 패키지

// 투표를 찾을 수 없을 때 발생하는 예외
public class VoteNotFoundException extends RuntimeException {  // RuntimeException을 상속하여 예외로 처리
    // 직접 오류 메시지를 전달받는 생성
    public VoteNotFoundException(String message) {
        super(message);  // 부모 클래스 생성자에 메시지 전달
    }
    
    // 투표 ID를 받아 표준화된 오류 메시지를 생성하는 생성자
    public VoteNotFoundException(Long id) {
        super("투표를 찾을 수 없습니다. ID: " + id);  // ID를 포함한 오류 메시지 생성
    }
}
