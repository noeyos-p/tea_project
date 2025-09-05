package com.project.tea.service;

import com.project.tea.dto.ResultDto;
import com.project.tea.dto.TeaDto;
import com.project.tea.entity.MoodEntity;
import com.project.tea.entity.ResultEntity;
import com.project.tea.entity.StateEntity;
import com.project.tea.repository.MoodRepository;
import com.project.tea.repository.ResultRepository;
import com.project.tea.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 추천 결과(ResultEntity) 저장 서비스
 * Mood와 State 결과 모두 저장 가능
 */
@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;
    private final MoodRepository moodRepository;
    private final StateRepository stateRepository;

    /**
     * Mood 결과 저장
     *
     * @param moodId 선택된 Mood ID
     * @param teas 추천 티 리스트
     * @param message 결과 메시지
     * @return 저장된 결과를 담은 ResultDto
     */
    @Transactional
    public ResultDto saveMoodResult(Long moodId, List<TeaDto> teas, String message) {
        MoodEntity mood = moodRepository.findById(moodId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Mood ID: " + moodId));

        ResultEntity result = new ResultEntity();
        result.setMood(mood);
        result.setResult(message);

        resultRepository.save(result);

        return ResultDto.fromDto(message, teas, result.getId());
    }

    /**
     * State 결과 저장
     *
     * @param stateId 선택된 State ID
     * @param teas 추천 티 리스트
     * @param message 결과 메시지
     * @return 저장된 결과를 담은 ResultDto
     */
    @Transactional
    public ResultDto saveStateResult(Long stateId, List<TeaDto> teas, String message) {
        StateEntity state = stateRepository.findById(stateId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 State ID: " + stateId));

        ResultEntity result = new ResultEntity();
        result.setState(state);
        result.setResult(message);

        resultRepository.save(result);

        return ResultDto.fromDto(message, teas, result.getId());
    }
}
