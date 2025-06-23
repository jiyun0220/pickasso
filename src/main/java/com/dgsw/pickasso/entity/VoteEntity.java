package com.dgsw.pickasso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "tb_vote")
public class VoteEntity {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;


        @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OptionEntity> options;
    }

