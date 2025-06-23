package com.dgsw.pickasso.controller;

import com.dgsw.pickasso.dto.ResponseDTO;
import com.dgsw.pickasso.dto.VoteDTO;
import com.dgsw.pickasso.entity.VoteEntity;
import com.dgsw.pickasso.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class VoteController {
    @Autowired
    private VoteService voteService;

    @PostMapping("/votes")
    public ResponseEntity<Map<String, Object>> registerVote(@RequestBody VoteDTO voteDTO){
        log.info("vote title:{}", voteDTO.getTitle());
        log.info("vote options:{}", voteDTO.getOptions());

        Long voteId = voteService.registerVote(voteDTO);

        if (voteId != null) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "voteId", voteId,
                    "message", "Vote created successfully"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Failed to create vote"
            ));
        }
    }

    @GetMapping("/votes")
    public List<VoteDTO> getAllVotes() {
        return voteService.getAllVotes();
    }



    @GetMapping("/votes/{id}")
    public ResponseEntity<VoteDTO> getVoteById(@PathVariable Long id) {
        VoteDTO voteDTO = voteService.getVoteById(id);
        if (voteDTO != null) {
            return ResponseEntity.ok(voteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
