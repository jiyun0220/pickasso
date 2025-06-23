package com.dgsw.pickasso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_option")
public class OptionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Integer count;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "vote_id")
    private VoteEntity vote;
}
