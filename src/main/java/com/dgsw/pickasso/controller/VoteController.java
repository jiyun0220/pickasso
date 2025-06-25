package com.dgsw.pickasso.controller;

// 필요한 클래스들 import
import com.dgsw.pickasso.dto.ResponseDTO;
import com.dgsw.pickasso.dto.VoteDTO;
import com.dgsw.pickasso.dto.VoteParticipateDTO;
import com.dgsw.pickasso.service.VoteService;
import jakarta.validation.Valid;  // 입력값 검증 어노테이션
import lombok.extern.slf4j.Slf4j;  // 로깅을 위한 lombok
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;  // HTTP 응답을 감싸는 클래스
import org.springframework.web.bind.annotation.*;  // REST 컨트롤러 관련 어노테이션

import java.util.List;

@Slf4j  // 로깅 기능 활성화
@RestController  // REST API 컨트롤러 지정
public class VoteController {  // 투표 관련 요청 처리 컨트롤러
    @Autowired  // 서비스 자동 주입
    private VoteService voteService;  // 투표 관련 비즈니스 로직 처리 서비스

    @PostMapping("/votes")  // POST /votes 요청 처리 - 새 투표 생성
    public ResponseEntity<ResponseDTO<Long>> registerVote(@Valid @RequestBody VoteDTO voteDTO){
        log.info("vote title:{}", voteDTO.getTitle());  // 생성할 투표 제목 로깅
        log.info("vote options:{}", voteDTO.getOptions());  // 투표 옵션들 로깅

        Long voteId = voteService.registerVote(voteDTO);  // 서비스에 투표 생성 요청하고 ID 받아옴
        return ResponseEntity.ok( 
            new ResponseDTO(true, "Vote created successfully", voteId)  // 성공 응답 데이터 생성
        );
    }

    @GetMapping("/votes")  // GET /votes 요청 처리 - 모든 투표 목록 조회
    public ResponseEntity<ResponseDTO<List<VoteDTO>>> getAllVotes() {
        List<VoteDTO> votes = voteService.getAllVotes();  // 서비스에서 모든 투표 목록 가져옴
        return ResponseEntity.ok(   
            new ResponseDTO(true, "Votes retrieved successfully", votes)  // 성공 응답 데이터 생성
        );
    }

    @GetMapping("/votes/{id}")  // GET /votes/{id} 요청 처리 - ID로 특정 투표 조회
    public ResponseEntity<ResponseDTO<VoteDTO>> getVoteById(@PathVariable Long id) {  // URL 경로에서 id 추출
        VoteDTO voteDTO = voteService.getVoteById(id);  // 서비스에서 해당 ID의 투표 정보 가져옴
        return ResponseEntity.ok( 
            new ResponseDTO(true, "Vote retrieved successfully", voteDTO)  // 성공 응답 데이터 생성
        );
    }

    @PutMapping("/votes/{id}")  // PUT /votes/{id} 요청 처리 - 기존 투표 수정
    public ResponseEntity<ResponseDTO> updateVote(@PathVariable Long id, @Valid @RequestBody VoteDTO voteDTO) {  // URL에서 id 추출 및 요청 본문에서 수정 정보 추출
        voteService.updateVote(id, voteDTO);  // 서비스에 투표 수정 요청
        return ResponseEntity.ok( 
            new ResponseDTO(true, "Vote updated successfully", id)  // 성공 응답 데이터 생성
        );
    }

    @DeleteMapping("/votes/{id}")  // DELETE /votes/{id} 요청 처리 - 투표 삭제
    public ResponseEntity<ResponseDTO> deleteVote(@PathVariable Long id) {  // URL에서 삭제할 투표 ID 추출
        voteService.deleteVote(id);  // 서비스에 투표 삭제 요청
        return ResponseEntity.ok( 
            new ResponseDTO(true, "Vote deleted successfully")  // 성공 응답 데이터 생성
        );
    }

    @PostMapping("/votes/{id}/vote")  // POST /votes/{id}/vote 요청 처리 - 투표 참여
    public ResponseEntity<ResponseDTO> participateInVote(@PathVariable Long id, @Valid @RequestBody VoteParticipateDTO participateDTO) {  // URL에서 투표 ID 추출 및 요청 본문에서 선택한 옵션 ID 추출
        voteService.participateInVote(id, participateDTO.getOptionId());  // 서비스에 투표 참여 요청
        return ResponseEntity.ok(  
            new ResponseDTO(true, "Vote participated successfully")  // 성공 응답 데이터 생성
        );
    }
}
