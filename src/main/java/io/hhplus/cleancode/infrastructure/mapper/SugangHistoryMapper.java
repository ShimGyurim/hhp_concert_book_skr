package io.hhplus.cleancode.infrastructure.mapper;

import io.hhplus.cleancode.domain.entity.SugangHistory;
import io.hhplus.cleancode.infrastructure.entity.SugangEntity;
import io.hhplus.cleancode.infrastructure.entity.SugangHistoryEntity;
import io.hhplus.cleancode.infrastructure.entity.SugangScheduleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class SugangHistoryMapper {

    @Autowired
    static ModelMapper modelMapper;

    public static SugangHistoryEntity sugangHistoryToEntity  (SugangHistory sugangHistory) {
        return modelMapper.map(sugangHistory,SugangHistoryEntity.class);
    }

    public static SugangHistory sugangHistoryToPojo (SugangHistoryEntity sugangHistoryEntity) {
        return modelMapper.map(sugangHistoryEntity,SugangHistory.class);
    }

    public static List<SugangHistoryEntity> sugangHistoryToEntityList (List<SugangHistory> sugangHistories) {
        return sugangHistories.stream()
                .map(pojo -> {
                    return modelMapper.map(pojo,SugangHistoryEntity.class);
                })
                .collect(Collectors.toList());
    }

    public static List<SugangHistory> sugangHistoryToPojoList (List<SugangHistoryEntity> sugangHistories) {
        return sugangHistories.stream()
                .map(item -> {
                    return modelMapper.map(item,SugangHistory.class);
                })
                .collect(Collectors.toList());
    }
}
