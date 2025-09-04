package com.project.tea.cafeApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class OutputDto {
    private String name;
    private String address;
    private String distance; // 미터 단위
    private String directionURL; // 길안내 링크
}
