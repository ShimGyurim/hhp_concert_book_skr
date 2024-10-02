package io.hhplus.cleancode.infrastructure.repository;

import io.hhplus.cleancode.domain.entity.Sugang;
import io.hhplus.cleancode.domain.repository.SugangRepository;
import io.hhplus.cleancode.infrastructure.entity.SugangEntity;
import io.hhplus.cleancode.infrastructure.mapper.SugangMapper;

public class SugangRepositoryImpl implements SugangRepository {

    private final SugangJpaRepository sugangJpaRepository;

    public SugangRepositoryImpl(SugangJpaRepository sugangJpaRepository) {
        this.sugangJpaRepository=sugangJpaRepository;
    }

    public <S extends Sugang> S save(S item) {
        SugangEntity sugangEntity = sugangJpaRepository.save(SugangMapper.SugangToEntity(item));

        return (S) SugangMapper.SugangToPojo(sugangEntity);
    };
}
