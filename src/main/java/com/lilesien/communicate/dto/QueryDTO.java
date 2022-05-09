package com.lilesien.communicate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDTO {
    private String search;
    private String tag;
    private Integer size;
    private Integer offset;
}
