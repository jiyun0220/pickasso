package com.dgsw.pickasso.repository;

import com.dgsw.pickasso.entity.OptionEntity;
import com.dgsw.pickasso.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long> {
    List<OptionEntity> findByVote(VoteEntity vote);

    Optional<OptionEntity> findById(Long id);
}
