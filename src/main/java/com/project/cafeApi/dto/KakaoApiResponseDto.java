package com.project.cafeApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KakaoApiResponseDto {
    @JsonProperty("documents")
    private List<DocumentDto> documentList;

    @JsonProperty("meta")
    private MetaDto metaDto;
}


//최상위응답