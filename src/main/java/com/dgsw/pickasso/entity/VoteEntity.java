package com.dgsw.pickasso.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;  // JSON 변환 시 필드 제외
import com.fasterxml.jackson.annotation.JsonInclude;  // JSON 변환 시 필드 포함 조건
import jakarta.persistence.*;  // JPA 어노테이션 모음

import lombok.Getter;  
import lombok.Setter;

import java.util.List;  // 기본 리스트 사용


import jakarta.persistence.Entity;  // 엔티티 지정 어노테이션
import jakarta.persistence.Id;  // 기본키 지정 어노테이션
import jakarta.persistence.Table;  // 테이블 이름 지정 어노테이션

@Entity  // JPA 엔티티로 지정 - 데이터베이스 테이블과 매핑
@Getter
@Setter
@Table(name = "tb_vote")  // 데이터베이스 테이블 이름 지정
public class VoteEntity {  // 투표 정보를 다루는 엔티티 클래스
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본키, 자동증가
        private Long id;  // 투표 고유 아이디
        private String title;  // 투표 제목


        @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, orphanRemoval = true)  // 1:다 관계, 연결된 옵션들 관리
        // mappedBy="vote": OptionEntity의 vote 필드와 연결
        // cascade=CascadeType.ALL: 투표 변경시 모든 관련 옵션도 함께 변경
        // orphanRemoval=true: 관계가 끼어진 옵션 자동 삭제
        private List<OptionEntity> options;  // 해당 투표의 옵션들
    }

