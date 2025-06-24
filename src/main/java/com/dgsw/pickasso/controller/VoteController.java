package com.dgsw.pickasso.controller;

import com.dgsw.pickasso.dto.ResponseDTO;
import com.dgsw.pickasso.dto.VoteDTO;
import com.dgsw.pickasso.dto.VoteParticipateDTO;
import com.dgsw.pickasso.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class VoteController {
    @Autowired
    private VoteService voteService;

    @PostMapping("/votes")
    public ResponseEntity<ResponseDTO<Long>> registerVote(@RequestBody VoteDTO voteDTO){
        log.info("vote title:{}", voteDTO.getTitle());
        log.info("vote options:{}", voteDTO.getOptions());

        Long voteId = voteService.registerVote(voteDTO);

        if (voteId != null) {
            return ResponseEntity.ok(
                new ResponseDTO(true, "Vote created successfully", voteId)
            );
        } else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Failed to create vote"));
        }
    }

    @GetMapping("/votes")
    public ResponseEntity<ResponseDTO<List<VoteDTO>>> getAllVotes() {
        List<VoteDTO> votes = voteService.getAllVotes();
        return ResponseEntity.ok(
            new ResponseDTO(true, "Votes retrieved successfully", votes)
        );
    }



    @GetMapping("/votes/{id}")
    public ResponseEntity<ResponseDTO<VoteDTO>> getVoteById(@PathVariable Long id) {
        VoteDTO voteDTO = voteService.getVoteById(id);
        if (voteDTO != null) {
            return ResponseEntity.ok(
                new ResponseDTO(true, "Vote retrieved successfully", voteDTO)
            );
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "Vote not found"));
        }
    }

    @PutMapping("/votes/{id}")
    public ResponseEntity<ResponseDTO> updateVote(@PathVariable Long id, @RequestBody VoteDTO voteDTO) {
        boolean success = voteService.updateVote(id, voteDTO);
        if (success) {
            return ResponseEntity.ok(
                new ResponseDTO(true, "Vote updated successfully", id)
            );
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "Vote not found"));
        }
    }

    @DeleteMapping("/votes/{id}")
    public ResponseEntity<ResponseDTO> deleteVote(@PathVariable Long id) {
        boolean success = voteService.deleteVote(id);
        if (success) {
            return ResponseEntity.ok(
                new ResponseDTO(true, "Vote deleted successfully")
            );
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(false, "Vote not found"));
        }
    }

    @PostMapping("/votes/{id}/vote")
    public ResponseEntity<ResponseDTO> participateInVote(@PathVariable Long id, @RequestBody VoteParticipateDTO participateDTO) {
        boolean success = voteService.participateInVote(id, participateDTO.getOptionId());
        if (success) {
            return ResponseEntity.ok(
                new ResponseDTO(true, "Vote participated successfully")
            );
        } else {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Invalid vote or option"));
        }
    }
}
