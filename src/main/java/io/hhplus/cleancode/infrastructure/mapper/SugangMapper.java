package io.hhplus.cleancode.infrastructure.mapper;

import io.hhplus.cleancode.domain.entity.Sugang;
import io.hhplus.cleancode.infrastructure.entity.SugangEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class SugangMapper {

    @Autowired
    static ModelMapper modelMapper;

    public static SugangEntity SugangToEntity  (Sugang sugang) {
        return modelMapper.map(sugang,SugangEntity.class);
    }

    public static Sugang SugangToPojo (SugangEntity sugangEntity) {
        return modelMapper.map(sugangEntity,Sugang.class);
    }
}
