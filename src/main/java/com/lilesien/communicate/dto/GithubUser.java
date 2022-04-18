package com.lilesien.communicate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
