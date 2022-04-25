package com.lilesien.communicate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//评论数据传输层
public class CommentCreateDTO {
    private Integer parentId;
    private String content;
    private Integer type;
}
