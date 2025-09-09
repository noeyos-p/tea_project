package com.project.tea.service;

import com.project.tea.dto.ChooseDto;
import com.project.tea.entity.ChooseEntity;
import com.project.tea.entity.TeaEntity;
import com.project.tea.repository.ChooseRepository;
import com.project.tea.repository.TeaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChooseService {
    private final ChooseRepository chooseRepository;
    private final TeaRepository teaRepository;

    /** 메인 집계: 선택 수 내림차순 상위 N */
    @Transactional(readOnly = true)
    public List<ChooseDto> findTopChoices(int limit) {
        return chooseRepository.findAllByOrderByCountDesc(PageRequest.of(0, limit))
                .stream()
                .map(ChooseDto::fromEntity)
                .toList();
    }

    /** teaId에 해당하는 choose 행이 없으면 생성(0으로) */
    @Transactional
    public void ensureRow(Long teaId) {
        chooseRepository.findByTea_Id(teaId).orElseGet(() -> {
            TeaEntity tea = teaRepository.findById(teaId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 차: " + teaId));
            ChooseEntity e = new ChooseEntity();
            e.setTea(tea);
            e.setCount(0L);
            return chooseRepository.save(e);
        });
    }

    /** 선택 시 카운트 +1 (행 보장 후 증가, 경합 시 1회 재시도) */
    @Transactional
    public void increment(Long teaId) {
        ensureRow(teaId);
        int updated = chooseRepository.incrementCount(teaId);
        if (updated == 0) { // 경합 등으로 실패 시 1회 더 보장 후 재시도
            ensureRow(teaId);
            chooseRepository.incrementCount(teaId);
        }
    }

    /** 단일 차의 누적 선택 수 조회 */
    @Transactional(readOnly = true)
    public long getCount(Long teaId) {
        return chooseRepository.findByTea_Id(teaId)
                .map(e -> e.getCount() == null ? 0L : e.getCount())
                .orElse(0L);
    }
}
