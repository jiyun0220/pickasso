package com.dgsw.pickasso.service;  // 서비스 패키지

// 필요한 클래스들 가져오기
import com.dgsw.pickasso.dto.OptionDTO;  // 옵션 데이터 전송 객체
import com.dgsw.pickasso.dto.VoteDTO;  // 투표 데이터 전송 객체
import com.dgsw.pickasso.entity.OptionEntity;  // 옵션 엔티티
import com.dgsw.pickasso.entity.VoteEntity;  // 투표 엔티티
import com.dgsw.pickasso.exception.InvalidVoteOperationException;  // 유효하지 않은 투표 작업 예외
import com.dgsw.pickasso.exception.OptionNotFoundException;  // 옵션을 찾을 수 없을 때 예외
import com.dgsw.pickasso.exception.VoteNotFoundException;  // 투표를 찾을 수 없을 때 예외
import com.dgsw.pickasso.repository.OptionRepository;  // 옵션 레포지토리
import com.dgsw.pickasso.repository.VoteRepository;  // 투표 레포지토리
import lombok.extern.slf4j.Slf4j;  // 로깅 기능
import org.springframework.beans.factory.annotation.Autowired;  // 의존성 주입 어노테이션
import org.springframework.stereotype.Service;  // 서비스 어노테이션
import org.springframework.transaction.annotation.Transactional;  // 트랜잭션 처리 어노테이션

// 자바 표준 라이브러리
import java.util.ArrayList;  // ArrayList 사용
import java.util.List;  // List 사용
import java.util.Optional;  // null 안전 처리를 위한 Optional

@Slf4j  // 로깅 기능 활성화
@Service  // 스프링 서비스 지정 (비즈니스 로직 처리 클래스)
public class VoteService {  // 투표 관련 비즈니스 로직을 처리하는 서비스 클래스
    @Autowired  // 자동 주입
    VoteRepository voteRepository;  // 투표 데이터 처리를 위한 레포지토리
    @Autowired  // 자동 주입
    OptionRepository optionRepository;  // 옵션 데이터 처리를 위한 레포지토리

    @Transactional  // 메소드 전체가 하나의 트랜잭션으로 처리
    public Long registerVote(VoteDTO voteDTO) {  // 새로운 투표 등록 기능
        try {
            // 투표 엔티티 생성 및 제목 설정
            VoteEntity voteEntity = new VoteEntity();
            voteEntity.setTitle(voteDTO.getTitle());

            // 옵션들을 저장할 리스트 생성
            List<OptionEntity> optionEntities = new ArrayList<>();
            for (OptionDTO optionDTO : voteDTO.getOptions()) {  // 전달된 모든 옵션 처리
                OptionEntity option = new OptionEntity();  // 새 옵션 엔티티 생성
                option.setContent(optionDTO.getContent());  // 옵션 내용 설정
                option.setCount(0);  // 초기 투표 수는 0
                option.setVote(voteEntity);  // 옵션이 소속된 투표 설정
                optionEntities.add(option);
            }
            voteEntity.setOptions(optionEntities);  // 투표에 옵션들 설정

            // 데이터베이스에 저장 후 ID 반환
            VoteEntity savedVote = voteRepository.save(voteEntity);
            return savedVote.getId();
        } catch (Exception e) {  // 예외 처리
            log.error("Error saving vote: {}", e.getMessage());  // 오류 로깅
            return null;
        }
    }

    @Transactional  // 트랜잭션 처리
    public boolean updateVote(Long voteId, VoteDTO voteDTO) {  // 기존 투표 업데이트 기능
        try {
            // 투표 엔티티 조회 - ID로 검색
            Optional<VoteEntity> voteEntityOpt = voteRepository.findById(voteId);
            if (!voteEntityOpt.isPresent()) {  // 해당 ID의 투표 없음
                throw new VoteNotFoundException(voteId);  // 투표 없음 예외 발생
            }

            VoteEntity voteEntity = voteEntityOpt.get();  // Optional에서 투표 엔티티 가져오기
            
            // 투표 제목 업데이트
            voteEntity.setTitle(voteDTO.getTitle());

            // 기존 옵션 리스트 가져오기
            List<OptionEntity> existingOptions = voteEntity.getOptions();

            // 기존 옵션들 처리
            if (existingOptions != null) {  // 기존 옵션이 있는 경우
                existingOptions.clear();  // 모두 제거 (투표가 전체 교체됨)
            } else {  // 기존 옵션이 없는 경우
                existingOptions = new ArrayList<>();  // 새 리스트 생성
                voteEntity.setOptions(existingOptions);  // 투표에 옵션 리스트 설정
            }

            // 새 옵션들 추가
            for (OptionDTO optionDTO : voteDTO.getOptions()) {
                OptionEntity option = new OptionEntity();  // 새 옵션 엔티티 생성
                option.setContent(optionDTO.getContent());  // 옵션 내용 설정
                option.setCount(0); // 새로운 옵션은 투표수 0으로 초기화
                option.setVote(voteEntity);  // 옵션이 소속한 투표 엔티티 설정
                existingOptions.add(option);  // 옵션 리스트에 추가
            }
            
            // 변경된 투표와 옵션들 데이터베이스에 저장
            voteRepository.save(voteEntity);
            
            return true;  // 업데이트 성공
        } catch (VoteNotFoundException e) {
            throw e; // 투표 다시 찾을 수 없음 예외는 그대로 전파
        } catch (Exception e) {  // 기타 모든 예외 처리
            log.error("Error updating vote: {}", e.getMessage());  // 오류 로깅
            throw new InvalidVoteOperationException("투표 업데이트 중 오류 발생: " + e.getMessage());  // 커스텀 예외 발생
        }
    }

    @Transactional  // 트랜잭션 처리
    public boolean deleteVote(Long voteId) {  // 투표 삭제 기능
        // 해당 ID의 투표 존재 여부 확인
        if (!voteRepository.existsById(voteId)) {  // 투표가 없는 경우
            throw new VoteNotFoundException(voteId);  // 투표 없음 예외 발생
        }
        voteRepository.deleteById(voteId);  // 데이터베이스에서 해당 ID의 투표 삭제
        return true;  // 삭제 성공
    }

    @Transactional  // 트랜잭션 처리
    public boolean participateInVote(Long voteId, Long optionId) {  // 투표에 참여하는 기능(투표하기)
        try {
            // 선택한 투표가 존재하는지 확인
            if (!voteRepository.existsById(voteId)) {  // 투표가 없는 경우
                throw new VoteNotFoundException(voteId);  // 투표 없음 예외 발생
            }
            
            // 선택한 옵션 가져오기
            Optional<OptionEntity> optionOpt = optionRepository.findById(optionId);
            if (!optionOpt.isPresent()) {  // 옵션이 없는 경우
                throw new OptionNotFoundException(optionId);  // 옵션 없음 예외 발생
            }
            
            OptionEntity option = optionOpt.get();  // Optional에서 옵션 가져오기
            
            // 해당 옵션이 요청한 투표에 속하는지 매칭 확인
            if (option.getVote() == null || !option.getVote().getId().equals(voteId)) {  // 옵션과 투표가 매칭되지 않음
                throw new InvalidVoteOperationException(  // 유효하지 않은 투표 작업 예외 발생
                    String.format("옵션 %d는 투표 %d에 속하지 않습니다", optionId, voteId));
            }

            // 투표 수 증가 - 해당 옵션에 투표했음
            option.setCount(option.getCount() + 1);  // 투표 수 1 증가
            optionRepository.save(option);  // 변경된 옵션 저장
            return true;  // 투표 참여 성공
        } catch (VoteNotFoundException | OptionNotFoundException | InvalidVoteOperationException e) {  // 사용자 정의 예외들
            throw e; // 이미 가능한 예외들은 그대로 전파
        } catch (Exception e) {  // 기타 예외 처리
            log.error("Error participating in vote: {}", e.getMessage());  // 오류 로깅
            throw new InvalidVoteOperationException("투표 참여 중 오류 발생: " + e.getMessage());  // 커스텀 예외 발생
        }
    }

    // 투표 목록 전체를 가져오는 기능
    public List<VoteDTO> getAllVotes() {  // 모든 투표 조회 기능
        // 데이터베이스에서 모든 투표 엔티티 가져오기
        List<VoteEntity> voteEntities = voteRepository.findAll();
        List<VoteDTO> voteDTOs = new ArrayList<>();  // 결과를 담을 DTO 리스트

        // 각 투표 엔티티를 DTO로 변환
        for (VoteEntity voteEntity : voteEntities) {
            VoteDTO voteDTO = new VoteDTO();  // 새로운 DTO 생성
            voteDTO.setId(voteEntity.getId());  // ID 설정
            voteDTO.setTitle(voteEntity.getTitle());  // 제목 설정
            voteDTOs.add(voteDTO);  // 결과 리스트에 추가
        }

        return voteDTOs;  // 모든 투표의 간략한 정보 반환(옵션 제외)
    }


    // 투표 ID로 특정 투표의 상세 정보를 가져오는 기능
    public VoteDTO getVoteById(Long voteId) {  // ID로 투표 상세 조회 기능
        // ID로 투표 엔티티 타입으로 가져오기
        Optional<VoteEntity> voteEntityOpt = voteRepository.findById(voteId);

        if (!voteEntityOpt.isPresent()) {  // 해당 ID의 투표가 없을 때
            throw new VoteNotFoundException(voteId);  // 투표 없음 예외 발생
        }
        
        // 투표 엔티티 받아서 DTO로 변환 시작
        VoteEntity voteEntity = voteEntityOpt.get();  // Optional에서 엔티티 가져오기
        VoteDTO voteDTO = new VoteDTO();  // 새 DTO 생성
        voteDTO.setId(voteEntity.getId());  // 아이디 설정
        voteDTO.setTitle(voteEntity.getTitle());  // 제목 설정

        // 옵션 변환을 위한 결과 리스트 생성
        ArrayList<OptionDTO> optionDTOs = new ArrayList<>();
        List<OptionEntity> optionEntities = voteEntity.getOptions();  // 해당 투표의 모든 옵션 가져오기

        // 각 옵션을 DTO로 변환
        for (OptionEntity optionEntity : optionEntities) {
            OptionDTO optionDTO = new OptionDTO();  // 새 옵션 DTO 생성
            optionDTO.setContent(optionEntity.getContent());  // 옵션 내용 설정
            optionDTO.setCount(optionEntity.getCount());  // 투표 수 설정
            optionDTO.setId(optionEntity.getId());  // 옵션 ID 설정
            optionDTOs.add(optionDTO);  // 결과 리스트에 추가
        }

        voteDTO.setOptions(optionDTOs);  // 투표 DTO에 변환된 옵션들 설정
        return voteDTO;  // 완성된 투표 DTO 반환 (투표와 모든 옵션 정보 포함)
    }

}

