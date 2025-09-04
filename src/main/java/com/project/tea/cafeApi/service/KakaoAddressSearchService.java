package com.project.tea.cafeApi.service;


import com.project.tea.cafeApi.dto.DocumentDto;
import com.project.tea.cafeApi.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class KakaoAddressSearchService {
    private final RestTemplate restTemplate;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    private static final String KAKAO_LOCAL_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    public DocumentDto searchAddress(String address) {
        if (ObjectUtils.isEmpty(address)) return null;

        URI uri = UriComponentsBuilder.fromUriString(KAKAO_LOCAL_URL)
                .queryParam("query", address)
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        KakaoApiResponseDto response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoApiResponseDto.class).getBody();
        if (response == null || response.getDocumentList().isEmpty()) return null;
        return response.getDocumentList().get(0);
    }
}
