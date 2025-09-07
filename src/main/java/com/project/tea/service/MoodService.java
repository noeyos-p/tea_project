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

@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodRepository moodRepository;
    private final MoodCheckRepository moodCheckRepository;
    private final ResultService resultService;

    /**
     * 체크리스트 분석 후 추천 티 조회
     * @param selectedCheckIds 체크리스트에서 선택한 MoodCheck ID 리스트 (5개)
     * @return ResultDto (메시지 + 추천 티 리스트 + 선택된 Mood ID + "mood" 타입)
     */
    public ResultDto analyzeMood(List<Long> selectedCheckIds) {
        if (selectedCheckIds == null || selectedCheckIds.size() != 5) {
            throw new IllegalArgumentException("체크리스트는 반드시 5개 선택해야 합니다.");
        }

        // 1. MoodCheck → Mood 매핑
        List<MoodCheckEntity> selectedChecks = moodCheckRepository.findAllById(selectedCheckIds);

        Map<Long, Integer> moodScores = new HashMap<>();
        for (MoodCheckEntity check : selectedChecks) {
            if (check.getMood() != null) {
                Long moodId = check.getMood().getId();
                moodScores.put(moodId, moodScores.getOrDefault(moodId, 0) + 1);
            }
        }

        // 2. 점수 기준 상위 Mood 선택 (동점 시 우선순위: 우울>슬픔>화남>기쁨)
        List<Long> priorityOrder = List.of(1L, 2L, 3L, 4L); // 우울, 슬픔, 화남, 기쁨
        int maxScore = Collections.max(moodScores.values());
        Long selectedMoodId = moodScores.entrySet().stream()
                .filter(e -> e.getValue() == maxScore)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(priorityOrder::indexOf))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("추천할 Mood가 없습니다."));

        // 3. 선택된 Mood 엔티티 가져오기
        MoodEntity mood = moodRepository.findById(selectedMoodId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Mood ID: " + selectedMoodId));

        // 4. 추천 티 리스트 (Mood와 관련된 티)
        List<TeaDto> teas = mood.getTeas().stream()
                .map(TeaDto::fromEntity)
                .collect(Collectors.toList());

        // 5. DB에서 해당 Mood ID의 Result 메시지 가져오기
        String message = resultService.getRandomMoodMessage(selectedMoodId);

        // 6. ResultDto 반환
        return ResultDto.fromDto(message, teas, selectedMoodId, "mood");
    }
}
