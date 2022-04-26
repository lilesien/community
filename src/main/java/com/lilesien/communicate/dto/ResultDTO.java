package com.lilesien.communicate.dto;

import com.lilesien.communicate.exception.CustomizeErrorCode;
import com.lilesien.communicate.exception.CustomizeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO <T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {

        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(errorCode.getCode());
        resultDTO.setMessage(errorCode.getMessage());
        return resultDTO;
    }

    public static ResultDTO errorOf(Integer code, String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }


    public static ResultDTO ok() {
        return errorOf(200,"成功");
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(), ex.getMessage());
    }

    public static <T> ResultDTO ok(T t){
        ResultDTO resultDTO = ok();
        resultDTO.setData(t);
        return resultDTO;
    }
}
