package com.dgsw.pickasso.service;

import com.dgsw.pickasso.dto.OptionDTO;
import com.dgsw.pickasso.dto.VoteDTO;
import com.dgsw.pickasso.entity.OptionEntity;
import com.dgsw.pickasso.entity.VoteEntity;
import com.dgsw.pickasso.repository.OptionRepository;
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
    @Autowired
    OptionRepository optionRepository;

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
            return savedVote.getId();
        } catch (Exception e) {
            log.error("Error saving vote: {}", e.getMessage());
            return null;
        }
    }

    @Transactional
    public boolean updateVote(Long voteId, VoteDTO voteDTO) {
        try {
            // 투표 엔티티 조회
            Optional<VoteEntity> voteEntityOpt = voteRepository.findById(voteId);
            if (!voteEntityOpt.isPresent()) {
                return false;
            }

            VoteEntity voteEntity = voteEntityOpt.get();
            
            // 타이틀 업데이트
            voteEntity.setTitle(voteDTO.getTitle());

            List<OptionEntity> existingOptions = voteEntity.getOptions();

            if (existingOptions != null) {
                existingOptions.clear();
            } else {
                existingOptions = new ArrayList<>();
                voteEntity.setOptions(existingOptions);
            }

            for (OptionDTO optionDTO : voteDTO.getOptions()) {
                OptionEntity option = new OptionEntity();
                option.setContent(optionDTO.getContent());
                option.setCount(0); // 새로운 옵션은 투표수 0으로
                option.setVote(voteEntity);
                existingOptions.add(option);
            }
            
            // 변경사항 저장
            voteRepository.save(voteEntity);
            
            return true;
        } catch (Exception e) {
            log.error("Error updating vote: {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteVote(Long voteId) {
        if (!voteRepository.existsById(voteId)) {
            return false;
        }
        voteRepository.deleteById(voteId);
        return true;
    }

    @Transactional
    public boolean participateInVote(Long voteId, Long optionId) {
        try {
            // 옵션 조회
            Optional<OptionEntity> optionOpt = optionRepository.findById(optionId);
            if (!optionOpt.isPresent()) {
                log.error("Option not found with ID: {}", optionId);
                return false;
            }
            
            OptionEntity option = optionOpt.get();
            
            // 해당 옵션이 요청한 투표에 속하는지 확인
            if (option.getVote() == null || !option.getVote().getId().equals(voteId)) {
                log.error("Option {} does not belong to vote {}", optionId, voteId);
                return false;
            }

            // 투표 수 증가
            option.setCount(option.getCount() + 1);
            optionRepository.save(option);
            return true;
        } catch (Exception e) {
            log.error("Error participating in vote: {}", e.getMessage());
            return false;
        }
    }

    public List<VoteDTO> getAllVotes() {
        List<VoteEntity> voteEntities = voteRepository.findAll();
        List<VoteDTO> voteDTOs = new ArrayList<>();

        for (VoteEntity voteEntity : voteEntities) {
            VoteDTO voteDTO = new VoteDTO();
            voteDTO.setId(voteEntity.getId());
            voteDTO.setTitle(voteEntity.getTitle());
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
                optionDTO.setId(optionEntity.getId());
                optionDTOs.add(optionDTO);
            }

            voteDTO.setOptions(optionDTOs);
            return voteDTO;
        }

        return null;
    }

}

