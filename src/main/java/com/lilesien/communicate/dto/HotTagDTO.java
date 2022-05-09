package com.lilesien.communicate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotTagDTO implements Comparable<HotTagDTO>{

    private String titleName;
    private Integer hotCount;

    @Override
    public int compareTo(@NotNull HotTagDTO o) {
        return this.hotCount - o.hotCount;
    }
}
