package com.project.cafeApi.service;


import com.project.cafeApi.dto.DocumentDto;
import com.project.cafeApi.dto.KakaoApiResponseDto;
import com.project.cafeApi.dto.OutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoCategorySearchService {
    private final RestTemplate restTemplate;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    private static final String KAKAO_CATEGORY_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    public List<OutputDto> searchCafe(String keyword, double latitude, double longitude, int radius) {
        URI uri = UriComponentsBuilder.fromUriString(KAKAO_CATEGORY_URL)
                .queryParam("query", keyword)
                .queryParam("category_group_code", "CE7") // 카페
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .queryParam("radius", radius)
                .queryParam("size", 10)
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        KakaoApiResponseDto response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoApiResponseDto.class).getBody();
        List<OutputDto> outputList = new ArrayList<>();
        if (response != null) {
            for (DocumentDto doc : response.getDocumentList()) {
                outputList.add(new OutputDto(
                        doc.getPlaceName(),
                        doc.getAddressName(),
                        doc.getDistance(),
                        doc.getPlaceUrl()
                ));
            }
        }
        return outputList;
    }
}
