package io.hhplus.cleancode.domain.service;

import io.hhplus.cleancode.application.dto.ItemDto;

import io.hhplus.cleancode.infrastructure.entity.ItemEntity;
//import io.hhplus.cleancode.mapper.QuickMapper;
import io.hhplus.cleancode.infrastructure.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class QuickService {

//    @Autowired
//    private QuickMapper quickMapper;

    @Autowired
    private ItemRepository itemRepository;

    public boolean registerItem(ItemDto itemDto){
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(itemDto.getId());
        itemEntity.setName(itemDto.getName());

        System.out.println("값확인 "+itemDto.getId());
        itemRepository.save(itemEntity);

        return true;
    }
    public ItemDto getItemById(String id) {
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("id",id);
//
//        HashMap<String, Object> res = quickMapper.findById(paramMap);
//
//        ItemDto itemDto = new ItemDto();
//        itemDto.setId((String) res.get("ID"));
//        itemDto.setName((String) res.get("NAME"));
//
//        return itemDto;
        ItemEntity itemEntity = itemRepository.findById(id).get();
//        ItemEntity itemEntity = null;
        System.out.println("itemEn "+itemEntity.getId());

        ItemDto itemDto = new ItemDto();

        itemDto.setId(itemEntity.getId());
        itemDto.setName(itemEntity.getName());

        return itemDto;
    }
}
