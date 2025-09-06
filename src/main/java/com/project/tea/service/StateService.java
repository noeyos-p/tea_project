package com.project.tea.service;

import com.project.tea.dto.ResultDto;
import com.project.tea.dto.TeaDto;
import com.project.tea.entity.StateEntity;
import com.project.tea.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * State 단일 선택 추천 티 조회
 */
@Service
@RequiredArgsConstructor
public class StateService {

    private final StateRepository stateRepository;
    private final ResultService resultService;

    /**
     * 선택된 State에 맞는 추천 티 조회
     */
    public ResultDto recommendByState(Long stateId) {
        StateEntity state = stateRepository.findById(stateId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 State ID: " + stateId));

        List<TeaDto> teas = state.getTeas().stream()
                .map(TeaDto::fromEntity)
                .collect(Collectors.toList());

        // 랜덤 메시지 가져오기
        String message = resultService.getRandomStateMessage(stateId);

        return resultService.toResultDto(message, teas, stateId);
    }
}
