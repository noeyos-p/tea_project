package com.project.tea.service;

import com.project.tea.dto.ResultDto;
import com.project.tea.dto.TeaDto;
import com.project.tea.entity.MoodCheckEntity;
import com.project.tea.entity.MoodEntity;
import com.project.tea.repository.MoodCheckRepository;
import com.project.tea.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mood 체크리스트 분석 및 추천 차 조회 서비스
 */
@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodRepository moodRepository;
    private final MoodCheckRepository moodCheckRepository;

    /**
     * 체크리스트로 선택된 Mood를 분석하고, 추천 차를 조회
     * @param checkedIds 체크된 MoodCheckEntity ID 리스트 (5개 필수)
     * @return 메시지 + 추천 차 리스트 + 1순위 Mood ID (ResultDto)
     */
    public ResultDto analyzeMood(List<Long> checkedIds) {
        if (checkedIds == null || checkedIds.size() != 5) {
            throw new IllegalArgumentException("체크리스트는 반드시 5개 선택해야 합니다.");
        }

        // Mood별 점수 초기화
        Map<String, Integer> scores = new HashMap<>();
        List<MoodEntity> moods = moodRepository.findAll();
        for (MoodEntity m : moods) scores.put(m.getMood(), 0);

        // 체크리스트 점수 집계
        for (Long id : checkedIds) {
            MoodCheckEntity item = moodCheckRepository.findById(id).orElse(null);
            if (item == null) {
                throw new IllegalArgumentException("선택한 체크리스트 항목이 DB에 존재하지 않습니다: " + id);
            }

            String moodName = item.getMood().getMood();
            scores.put(moodName, scores.getOrDefault(moodName, 0) + 1);
        }

        // 우선순위 정의
        List<String> priority = Arrays.asList("우울", "슬픔", "화남", "기쁨");

        // 점수 기준 정렬 + 우선순위 적용
        List<String> sorted = new ArrayList<>(scores.keySet());
        sorted.sort((a, b) -> {
            int cmp = scores.get(b) - scores.get(a);
            if (cmp == 0) return Integer.compare(priority.indexOf(a), priority.indexOf(b));
            return cmp;
        });

        // 상위 2개 감정 선택
        List<String> topMoods = sorted.subList(0, Math.min(2, sorted.size()));

        // 결과 메시지 생성
        String message = topMoods.size() == 1 ?
                topMoods.get(0) + "하시군요." :
                topMoods.get(0) + "과 " + topMoods.get(1) + "이 느껴지시군요.";

        // 추천 차 조회 및 DTO 변환
        List<TeaDto> allTeas = new ArrayList<>();
        MoodEntity firstMood = null;

        for (String moodName : topMoods) {
            MoodEntity mood = moodRepository.findByMood(moodName).orElse(null);
            if (mood == null) continue; // DB에 없으면 그냥 건너뜀

            if (firstMood == null) firstMood = mood;

            List<TeaDto> teas = mood.getTeas().stream()
                    .filter(t -> t.getMood() != null && t.getState() == null)
                    .map(TeaDto::fromEntity)
                    .collect(Collectors.toList());

            allTeas.addAll(teas);
        }

        if (firstMood == null) {
            throw new IllegalArgumentException("추천할 Mood가 존재하지 않습니다.");
        }

        return ResultDto.fromDto(message, allTeas, firstMood.getId());
    }

}
