package com.project.tea.cafeApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class MetaDto {

    @JsonProperty("total_count")
    private int totalCount;
}

//응답관련 메타데이터
