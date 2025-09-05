package com.project.tea.service;

import com.project.tea.dto.TeaDto;
import com.project.tea.entity.StateEntity;
import com.project.tea.entity.TeaEntity;
import com.project.tea.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StateService {

    private final StateRepository stateRepository;

    @Transactional(readOnly = true)
    public List<TeaDto> recommendByState(Long stateId) {
        StateEntity state = stateRepository.findById(stateId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 State ID: " + stateId));

        List<TeaEntity> teas = state.getTeas();

        // 둘다 있거나 없으면 안됨(한쪽이 비어있거나 둘다 비어있으면 안된다는 뜻)
        for (TeaEntity t : teas) {
            if ((t.getMood() == null && t.getState() == null) || (t.getMood() != null && t.getState() != null)) {
                throw new IllegalArgumentException("Tea는 Mood 또는 State 중 하나만 가질 수 있습니다.");
            }
        }

        return teas.stream()
                .map(TeaDto::fromEntity)
                .collect(Collectors.toList());
    }
}


