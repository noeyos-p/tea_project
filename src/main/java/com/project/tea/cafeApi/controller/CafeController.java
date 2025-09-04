package com.project.tea.cafeApi.controller;


import com.project.tea.cafeApi.dto.DocumentDto;
import com.project.tea.cafeApi.dto.OutputDto;
import com.project.tea.cafeApi.service.KakaoAddressSearchService;
import com.project.tea.cafeApi.service.KakaoCategorySearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CafeController {

    private final KakaoAddressSearchService addressService;
    private final KakaoCategorySearchService categoryService;

    public CafeController(KakaoAddressSearchService addressService, KakaoCategorySearchService categoryService) {
        this.addressService = addressService;
        this.categoryService = categoryService;
    }

    // 검색 폼 페이지
    @GetMapping("/cafe/search")
    public String searchForm() {
        return "cafeSearch";
    }

    // 카페 추천 결과 페이지
    @PostMapping("/cafe/map")
    public String showCafeMap(String keyword, String address, Model model) {
        // 1. 주소 → 좌표 변환
        DocumentDto doc = addressService.searchAddress(address);
        if (doc == null) {
            model.addAttribute("error", "주소를 찾을 수 없습니다.");
            return "/cafe/cafeSearch";
        }

        // 2. 좌표 → 차 판매 카페 검색
        List<OutputDto> cafes = categoryService.searchCafe(keyword, doc.getLatitude(), doc.getLongitude(), 10000);

        // 3. 거리 기준 오름차순 정렬 후 상위 5곳만 선택
        cafes = cafes.stream()
                .sorted(Comparator.comparingInt(c ->
                        Integer.parseInt(c.getDistance().replaceAll("[^0-9]", ""))
                ))
                .limit(5)
                .collect(Collectors.toList());

        // 4. 모델에 담아서 뷰로 전달
        model.addAttribute("dto", cafes);
        model.addAttribute("keyword", keyword);

        return "/cafe/cafeMap"; // 결과 페이지 (지도 + 카페 리스트)
    }
}
