package com.rtomyj.yugiohAPI.helper.exceptions.mapper;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MapperDirectives {
    private String to;
    private String message;
    private String code;
}