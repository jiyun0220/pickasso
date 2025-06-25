package com.dgsw.pickasso.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;  // 공백만 있는 문자열 및 null 검증 어노테이션
import jakarta.validation.constraints.Size;  // 크기 제한 검증 어노테이션
import lombok.AllArgsConstructor;  // 모든 필드를 매개변수로 갖는 생성자 자동 생성
import lombok.Getter; 
import lombok.NoArgsConstructor;  // 기본 생성자 자동 생성
import lombok.Setter;  

@Getter 
@Setter 
@AllArgsConstructor  // 모든 필드를 매개변수로 갖는 생성자 자동 생성
@NoArgsConstructor  // 기본 생성자 자동 생성
public class OptionDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;  // 옵션 ID - 생성된 옵션의 고유 식별자
    
    @NotBlank(message = "옵션 내용은 필수 입력값입니다")  // 공백만 있는 문자열 및 null 검증 어노테이션
    @Size(min = 1, max = 100, message = "옵션 내용은 1자 이상 100자 이하로 입력해주세요")  // 옵션 내용 길이 제한 검증
    private String content;  // 옵션 내용/이름
    
    private Integer count;  // 해당 옵션에 지금까지 투표한 개수
}
