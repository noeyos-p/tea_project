package com.project.tea.cafeApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class DocumentDto {
    @JsonProperty("place_name")
    private String placeName;
    @JsonProperty("address_name")
    private String addressName;
    @JsonProperty("y")
    private double latitude;
    @JsonProperty("x")
    private double longitude;
    @JsonProperty("distance")
    private String distance; // String 타입, 미터 단위
    @JsonProperty("place_url")
    private String placeUrl;
}
