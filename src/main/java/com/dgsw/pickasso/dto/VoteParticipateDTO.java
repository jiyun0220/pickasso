package com.dgsw.pickasso.dto;  // DTO 패키지

import jakarta.validation.constraints.NotNull;  // 필드 검증을 위한 어노테이션
import lombok.AllArgsConstructor;  // 모든 필드를 매개변수로 갖는 생성자 자동 생성
import lombok.Getter; 
import lombok.NoArgsConstructor;  // 기본 생성자 자동 생성
import lombok.Setter;  

@Getter 
@Setter 
@AllArgsConstructor  // 모든 필드를 매개변수로 갖는 생성자 자동 생성
@NoArgsConstructor  // 기본 생성자 자동 생성
public class VoteParticipateDTO {  // 투표 참여 요청 데이터 전송 객체
    @NotNull(message = "옵션 ID는 필수 입력값입니다")  // null 값 검증 어노테이션, 에러 메시지 설정
    private Long optionId;  // 선택한 투표 옵션의 ID
}
