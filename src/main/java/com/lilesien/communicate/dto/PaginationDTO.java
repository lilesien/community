package com.lilesien.communicate.dto;

import com.lilesien.communicate.pojo.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO<T> {
    private List<T> data;
    //当前页
    private Integer currentPage;
    //页的总数
    private Integer totalPage;
    private boolean hasFirst;
    private boolean hasEnd;
    private boolean hasPrevious;
    private boolean hasNext;
    private List<Integer> pageList = new ArrayList<>();



    //当前页前后分别有多少页
    //currentPage
    public void setDetail(Integer currentPage, Integer pageCount, Integer size) {
        //当前页面
        this.currentPage = currentPage;
        //设置页面总数
        this.totalPage = pageCount;
        //设置是否有< << >> >图标
        this.hasPrevious = pageCount != 0 && currentPage != 1;
        this.hasNext = pageCount != 0 && currentPage != pageCount;
        this.hasFirst = currentPage > 4;
        this.hasEnd = currentPage < pageCount - 3;
        //设置页面标签的集合(如果存在)
        //前三页
        for(int i = 3; i >= 1; i--){
            if(currentPage - i > 0){
                pageList.add(currentPage - i);
            }
        }
        //当前页
        pageList.add(currentPage);
        //后三页
        for(int i = 1; i <= 3; i++){
            if(currentPage + i <= pageCount){
                pageList.add(currentPage + i);
            }
        }
    }
}
