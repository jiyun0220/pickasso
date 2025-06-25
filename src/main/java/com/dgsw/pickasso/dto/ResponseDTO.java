package com.dgsw.pickasso.dto;  // DTO 패키지

import lombok.AllArgsConstructor;  // 모든 필드를 매개변수로 갖는 생성자 자동 생성
import lombok.Getter; 
import lombok.NoArgsConstructor;  // 기본 생성자 자동 생성
import lombok.Setter;  

@Getter 
@Setter 
@NoArgsConstructor 
public class ResponseDTO<T> {  // API 응답을 위한 표준 형태 DTO, 제네릭 타입 T를 사용
    private String message;  // 응답 메시지 (성공/실패 이유 등)
    private boolean success;  // 작업 성공 여부 (true/false)
    private T data;  // 응답 데이터 (제네릭 타입으로 다양한 데이터 타입 지원)
    
    // 메시지만 있는 기본 성공 응답을 위한 생성자
    public ResponseDTO(String message) {
        this.message = message;  // 응답 메시지 설정
        this.success = true;  // 기본값은 성공(true)
    }
    
    // 성공/실패 여부와 메시지를 가진 응답을 위한 생성자
    public ResponseDTO(boolean success, String message) {
        this.success = success;  // 성공/실패 여부 설정
        this.message = message;  // 응답 메시지 설정
    }
    
    // 성공/실패 여부, 메시지, 반환 데이터를 모두 가진 완전한 응답을 위한 생성자
    public ResponseDTO(boolean success, String message, T data) {
        this.success = success;  // 성공/실패 여부 설정
        this.message = message;  // 응답 메시지 설정
        this.data = data;  // 응답 데이터 설정
    }
}
