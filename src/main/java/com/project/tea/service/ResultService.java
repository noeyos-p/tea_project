package com.project.tea.service;

import com.project.tea.dto.ResultDto;
import com.project.tea.dto.TeaDto;
import com.project.tea.entity.ResultEntity;
import com.project.tea.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ResultService
 * - Mood 또는 State에 맞는 랜덤 메시지 조회
 * - 저장 기능은 UserDataService에서 처리
 */
@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;

    /**
     * Mood ID에 맞는 랜덤 메시지 조회
     */
    public String getRandomMoodMessage(Long moodId) {
        ResultEntity randomMessage = resultRepository.findRandomByMood(moodId);
        if (randomMessage == null) {
            throw new IllegalArgumentException("해당 Mood의 메시지가 존재하지 않습니다: " + moodId);
        }
        return randomMessage.getResult();
    }


    public String getStateMessage(Long stateId) {
        ResultEntity message = resultRepository.findByState(stateId);
        if (message == null) {
            throw new IllegalArgumentException("해당 State의 메시지가 존재하지 않습니다: " + stateId);
        }
        return message.getResult();
    }

    /**
     * ResultDto 생성 (Teas + 랜덤 메시지)
     *
     * @param message 랜덤 메시지
     * @param teas 추천 티 리스트
     * @param resultId Mood/State ID
     * @return ResultDto
     */
    public ResultDto toResultDto(String message, java.util.List<TeaDto> teas, Long resultId) {
        return ResultDto.fromDto(message, teas, resultId);
    }
}
