package com.project.tea.service;

import com.project.tea.dto.ChooseDto;
import com.project.tea.repository.ChooseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChooseService {

    private final ChooseRepository chooseRepository;

    /** 집계수 내림차순 상위 N개 (메인에선 7 고정으로 호출) */
    @Transactional(readOnly = true)
    public List<ChooseDto> findTopChoices(int limit) {
        return chooseRepository.findAllByOrderByCountDesc(PageRequest.of(0, limit))
                .stream()
                .map(ChooseDto::fromEntity)
                .toList();
    }

    /** 선택 시 카운트 +1 (행이 없으면 생성하지 않음) */
    @Transactional
    public void increment(Long teaId) {
        int updated = chooseRepository.incrementCount(teaId);
        // updated == 0 이면 해당 tea의 choose 행이 없는 상태 → 생성 정책이 아니므로 아무 것도 하지 않음
    }

    /** 단일 차의 누적 선택 수 */
    @Transactional(readOnly = true)
    public long getCount(Long teaId) {
        return chooseRepository.findByTea_Id(teaId)
                .map(e -> e.getCount() == null ? 0L : e.getCount())
                .orElse(0L);
    }
}
