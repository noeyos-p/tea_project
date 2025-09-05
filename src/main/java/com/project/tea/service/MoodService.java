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

    @Transactional
    public ResultDto analyzeAndSaveMoodResult(List<Long> checkedIds) {
        if (checkedIds.size() != 5)
            throw new IllegalArgumentException("체크리스트는 반드시 5개 선택해야 합니다.");

        Map<String, Integer> scores = new HashMap<>();
        List<MoodEntity> moods = moodRepository.findAll();
        for (MoodEntity m : moods) scores.put(m.getMood(), 0);

        for (Long id : checkedIds) {
            MoodCheckEntity item = (MoodCheckEntity) moodCheckRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("체크리스트 없음: " + id));
            String moodName = String.valueOf(item.getMood());
            scores.put(moodName, scores.getOrDefault(moodName, 0) + 1);
        }

        List<String> priority = Arrays.asList("우울", "슬픔", "화남", "기쁨");
        List<String> sorted = new ArrayList<>(scores.keySet());
        sorted.sort((a, b) -> {
            int cmp = scores.get(b) - scores.get(a);
            if (cmp == 0) return Integer.compare(priority.indexOf(a), priority.indexOf(b));
            return cmp;
        });

        List<String> topMoods = sorted.subList(0, Math.min(2, sorted.size()));
        String message = topMoods.size() == 1 ?
                topMoods.get(0) + "하시군요." :
                topMoods.get(0) + "과 " + topMoods.get(1) + "이 느껴지시군요.";

        Map<String, List<TeaDto>> teaMap = new HashMap<>();
        List<TeaDto> allTeas = new ArrayList<>();
        MoodEntity firstMood = null;

        for (String moodName : topMoods) {
            MoodEntity mood = moodRepository.findByMood(moodName)
                    .orElseThrow(() -> new RuntimeException("해당 기분 없음: " + moodName));

            if (firstMood == null) firstMood = mood;

            List<TeaDto> teas = mood.getTeas().stream()
                    .filter(t -> t.getMood() != null && t.getState() == null)
                    .map(TeaDto::fromEntity)
                    .collect(Collectors.toList());

            teaMap.put(moodName, teas);
            allTeas.addAll(teas);
        }

        ResultEntity result = new ResultEntity();
        result.setResult(message);
        result.setMood(firstMood);
        resultRepository.save(result);

        return ResultDto.fromDto(message, allTeas, result.getId());
    }
}
