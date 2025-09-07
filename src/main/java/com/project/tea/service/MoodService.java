package com.project.tea.service;

import com.project.tea.dto.ResultDto;
import com.project.tea.dto.TeaDto;
import com.project.tea.entity.MoodEntity;
import com.project.tea.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mood 체크리스트 분석 및 추천 티 조회
 */
@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodRepository moodRepository;
    private final ResultService resultService;

    /**
     * 체크리스트 점수 분석 후 추천 티 조회
     * 체크리스트 결과 (Mood ID -> 점수)
     */
    public ResultDto analyzeMood(Map<Long, Integer> moodScores) {
        if (moodScores == null || moodScores.isEmpty()) {
            throw new IllegalArgumentException("Mood 점수가 존재하지 않습니다.");
        }

        // 우선순위 정의
        List<Long> priority = new ArrayList<>(List.of(
                getMoodIdByName("우울"),
                getMoodIdByName("슬픔"),
                getMoodIdByName("화남"),
                getMoodIdByName("기쁨")
        ));

        // 점수 기준 정렬
        List<Long> sortedMoods = new ArrayList<>(moodScores.keySet());
        sortedMoods.sort((a, b) -> {
            int cmp = moodScores.get(b) - moodScores.get(a);
            if (cmp == 0) return Integer.compare(priority.indexOf(a), priority.indexOf(b));
            return cmp;
        });

        // 상위 2개 Mood 선택
        List<Long> topMoods = sortedMoods.subList(0, Math.min(2, sortedMoods.size()));

        // 1순위 Mood로 추천 티 조회
        MoodEntity primaryMood = moodRepository.findById(topMoods.get(0))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Mood ID: " + topMoods.get(0)));

        List<TeaDto> teas = primaryMood.getTeas().stream()
                .map(TeaDto::fromEntity)
                .collect(Collectors.toList());

        // 랜덤 메시지는 ResultService에서 가져오기
        String message = resultService.getRandomMoodMessage(primaryMood.getId());

        // ResultDto 생성
        return resultService.toResultDto(message, teas, primaryMood.getId(),"mood");
    }

    private Long getMoodIdByName(String name) {
        return moodRepository.findByMood(name)
                .orElseThrow(() -> new IllegalArgumentException(name + " Mood가 존재하지 않습니다."))
                .getId();
    }
}
