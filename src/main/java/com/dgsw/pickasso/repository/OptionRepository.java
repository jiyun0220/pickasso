package com.dgsw.pickasso.repository;

import com.dgsw.pickasso.entity.OptionEntity;
import com.dgsw.pickasso.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long> {

}
