package com.project.tea.service;

import com.project.tea.dto.ResultDto;
import com.project.tea.dto.TeaDto;
import com.project.tea.entity.MoodCheckEntity;
import com.project.tea.entity.MoodEntity;
import com.project.tea.entity.ResultEntity;
import com.project.tea.repository.MoodCheckRepository;
import com.project.tea.repository.MoodRepository;
import com.project.tea.repository.ResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodRepository moodRepository;
    private final MoodCheckRepository moodCheckRepository;
    private final ResultRepository resultRepository;

    /**
     * 체크리스트로 선택된 Mood를 분석하고, 추천 차를 조회 후 결과를 저장
     * @param checkedIds 체크된 MoodCheckEntity ID 리스트 (5개 필수)
     * @return 추천 메시지와 추천 차 리스트를 담은 ResultDto
     */
    @Transactional
    public ResultDto analyzeAndSaveMoodResult(List<Long> checkedIds) {
        // 체크리스트는 반드시 5개 선택
        if (checkedIds.size() != 5)
            throw new IllegalArgumentException("체크리스트는 반드시 5개 선택해야 합니다.");

        // Mood별 점수 초기화
        Map<String, Integer> scores = new HashMap<>();
        List<MoodEntity> moods = moodRepository.findAll();
        for (MoodEntity m : moods) scores.put(m.getMood(), 0);

        // 체크리스트 점수 집계
        for (Long id : checkedIds) {
            MoodCheckEntity item = moodCheckRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("체크리스트 없음: " + id));

            // 각 체크리스트에 연결된 Mood 이름 가져오기
            String moodName = item.getMood().getMood();
            scores.put(moodName, scores.getOrDefault(moodName, 0) + 1);
        }

        // 우선순위 정의: 동점일 경우 적용
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
        Map<String, List<TeaDto>> teaMap = new HashMap<>();
        List<TeaDto> allTeas = new ArrayList<>();
        MoodEntity firstMood = null;

        for (String moodName : topMoods) {
            MoodEntity mood = moodRepository.findByMood(moodName)
                    .orElseThrow(() -> new RuntimeException("해당 기분 없음: " + moodName));

            // 결과 저장용 첫 번째 Mood
            if (firstMood == null) firstMood = mood;

            // Mood와 연결된 Tea만 필터링 (State가 없는 Tea)
            List<TeaDto> teas = mood.getTeas().stream()
                    .filter(t -> t.getMood() != null && t.getState() == null)
                    .map(TeaDto::fromEntity)
                    .collect(Collectors.toList());

            teaMap.put(moodName, teas);
            allTeas.addAll(teas);
        }

        // 결과 엔티티 생성 및 저장
        ResultEntity result = new ResultEntity();
        result.setResult(message);
        result.setMood(firstMood);
        resultRepository.save(result);

        // DTO 반환 (메시지 + 추천 차 + 결과 ID)
        return ResultDto.fromDto(message, allTeas, result.getId());
    }
}
