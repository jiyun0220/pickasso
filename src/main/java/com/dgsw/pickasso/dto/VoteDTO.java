package com.dgsw.pickasso.dto;

import com.fasterxml.jackson.annotation.JsonInclude;  // JSON 응답에서 null 값 필드 제외 어노테이션
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;  // 공백만 있는 문자열 및 null 검증 어노테이션
import jakarta.validation.constraints.NotEmpty;  // 비어있지 않은지 검증 어노테이션(컵렉션용)
import jakarta.validation.constraints.NotNull;  // null 값 검증 어노테이션
import jakarta.validation.constraints.Size;  // 크기 제한 검증 어노테이션
import lombok.AllArgsConstructor;  // 모든 필드를 매개변수로 갖는 생성자 자동 생성
import lombok.Getter; 
import lombok.NoArgsConstructor;  // 기본 생성자 자동 생성
import lombok.Setter;  

import java.util.ArrayList;  // 옵션 목록을 저장할 ArrayList

@Getter 
@Setter 
@AllArgsConstructor  // 모든 필드를 매개변수로 갖는 생성자 자동 생성
@NoArgsConstructor  // 기본 생성자 자동 생성
@JsonInclude(JsonInclude.Include.NON_NULL)  // JSON 변환 시 null 값을 가진 필드는 제외
public class VoteDTO {  // 투표 정보를 주고받기 위한 DTO 클래스
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;  // 투표 ID - 생성된 투표의 고유 식별자
    
    @NotBlank(message = "제목은 필수 입력값입니다")  // 공백만 있는 문자열 및 null 검증 어노테이션
    @Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이하로 입력해주세요")  // 제목 길이 제한 검증
    private String title;  // 투표 제목
    
    @NotEmpty(message = "최소 1개 이상의 투표 옵션이 필요합니다")  // 비어있지 않은지 검증 어노테이션
    private ArrayList<OptionDTO> options;  // 투표 옵션들을 담는 리스트
}
