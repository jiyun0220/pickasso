package com.dgsw.pickasso.service;

import com.dgsw.pickasso.dto.OptionDTO;
import com.dgsw.pickasso.dto.VoteDTO;
import com.dgsw.pickasso.entity.OptionEntity;
import com.dgsw.pickasso.entity.VoteEntity;
import com.dgsw.pickasso.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VoteService {
    @Autowired
    VoteRepository voteRepository;

    @Transactional
    public Long registerVote(VoteDTO voteDTO) {
        try {
            VoteEntity voteEntity = new VoteEntity();
            voteEntity.setTitle(voteDTO.getTitle());

            List<OptionEntity> optionEntities = new ArrayList<>();
            for (OptionDTO optionDTO : voteDTO.getOptions()) {
                OptionEntity option = new OptionEntity();
                option.setContent(optionDTO.getContent());
                option.setCount(0);
                option.setVote(voteEntity);
                optionEntities.add(option);
            }
            voteEntity.setOptions(optionEntities);

            VoteEntity savedVote = voteRepository.save(voteEntity);
            log.info("Vote saved successfully with ID: {}", savedVote.getId());
            return savedVote.getId(); // 저장된 투표의 ID 반환
        } catch (Exception e) {
            log.error("Error saving vote: ", e);
            return null; // 실패 시 null 반환
        }
    }

    public List<VoteDTO> getAllVotes() {
        List<VoteEntity> voteEntities = voteRepository.findAll();
        List<VoteDTO> voteDTOs = new ArrayList<>();

        for (VoteEntity voteEntity : voteEntities) {
            VoteDTO voteDTO = new VoteDTO();
            voteDTO.setId(voteEntity.getId()); // ID 추가
            voteDTO.setTitle(voteEntity.getTitle());
            // options는 설정하지 않음 (제목과 ID만)
            voteDTOs.add(voteDTO);
        }

        return voteDTOs;
    }


    public VoteDTO getVoteById(Long voteId) {
        Optional<VoteEntity> voteEntityOpt = voteRepository.findById(voteId);

        if (voteEntityOpt.isPresent()) {
            VoteEntity voteEntity = voteEntityOpt.get();
            VoteDTO voteDTO = new VoteDTO();
            voteDTO.setId(voteEntity.getId());
            voteDTO.setTitle(voteEntity.getTitle());

            ArrayList<OptionDTO> optionDTOs = new ArrayList<>();
            List<OptionEntity> optionEntities = voteEntity.getOptions();

            for (OptionEntity optionEntity : optionEntities) {
                OptionDTO optionDTO = new OptionDTO();
                optionDTO.setContent(optionEntity.getContent());
                optionDTO.setCount(optionEntity.getCount());
                optionDTOs.add(optionDTO);
            }

            voteDTO.setOptions(optionDTOs);
            return voteDTO;
        }

        return null; // 투표를 찾을 수 없음
    }

}

