package com.dgsw.pickasso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;  // JSON 변환 시 필드 제외
import jakarta.persistence.*;  // JPA 어노테이션 모음
import lombok.Getter;
import lombok.Setter;

@Entity  // JPA 엔티티로 지정 - 데이터베이스 테이블과 매핑
@Getter
@Setter
@Table(name = "tb_option")  // 데이터베이스 테이블 이름 지정
public class OptionEntity {  // 투표 옵션 정보를 다루는 엔티티 클래스
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본키, 자동증가
    private Long id;  // 옵션 고유 아이디
    private String content;  // 옵션 내용/이름
    private Integer count;  // 해당 옵션에 투표한 개수

    @ManyToOne(fetch = FetchType.LAZY)  // 다:일 관계, 지연 로딩 사용
    @JoinColumn(name = "vote_id")  // 외래키 열 이름 지정
    private VoteEntity vote;  // 이 옵션이 속한 투표 엔티티
}
