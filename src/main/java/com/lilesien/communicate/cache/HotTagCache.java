package com.lilesien.communicate.cache;

import com.lilesien.communicate.dto.HotTagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class HotTagCache {

    private Map<String, Integer> tagMap;

    private List<String> hots = new ArrayList<>();

    public List<String> getHotMessage(Map<String, Integer> tags){
        int max = 3;
        //优先队列，采用小顶堆的方式，在排好序后
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>();
        tags.forEach((name, priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setTitleName(name);
            hotTagDTO.setHotCount(priority);

            if(priorityQueue.size() < 5){
                priorityQueue.add(hotTagDTO);
            }else{
                if(priorityQueue.peek().compareTo(hotTagDTO) < 0){
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });

        //最热门的标签
        List<String> sortedTags = new ArrayList<>();

        while (!priorityQueue.isEmpty()){
            sortedTags.add(0,priorityQueue.poll().getTitleName());
        }
        System.out.println(sortedTags);
        hots = sortedTags;
        return hots;
    }
}
