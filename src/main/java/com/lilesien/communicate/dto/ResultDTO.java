package com.lilesien.communicate.dto;

import com.lilesien.communicate.exception.CustomizeErrorCode;
import com.lilesien.communicate.exception.CustomizeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO {
    private Integer code;
    private String message;

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return new ResultDTO(errorCode.getCode(),errorCode.getMessage());
    }

    public static ResultDTO errorOf(Integer code, String message){
        return new ResultDTO(code,message);
    }


    public static ResultDTO ok() {
        return new ResultDTO(200,"成功");
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return new ResultDTO(ex.getCode(), ex.getMessage());
    }
}
