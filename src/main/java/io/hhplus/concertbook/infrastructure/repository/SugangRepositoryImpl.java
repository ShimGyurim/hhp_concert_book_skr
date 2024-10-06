package io.hhplus.concertbook.infrastructure.repository;

import io.hhplus.concertbook.domain.entity.Sugang;
import io.hhplus.concertbook.domain.repository.SugangRepository;
import io.hhplus.concertbook.infrastructure.entity.SugangEntity;
import io.hhplus.concertbook.infrastructure.mapper.SugangMapper;

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
