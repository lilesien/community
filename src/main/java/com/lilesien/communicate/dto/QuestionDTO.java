package com.lilesien.communicate.dto;

import com.lilesien.communicate.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private Integer id;
    public String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private int commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private User user;
}
