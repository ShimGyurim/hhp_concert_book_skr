package io.hhplus.concertbook.infrastructure.repository;

import io.hhplus.concertbook.domain.entity.SugangSchedule;
import io.hhplus.concertbook.domain.repository.SugangScheduleRepository;
import io.hhplus.concertbook.infrastructure.entity.SugangScheduleEntity;
import io.hhplus.concertbook.infrastructure.mapper.SugangScheduleMapper;

import java.util.List;
import java.util.Optional;

public class SugangScheduleRepositoryImpl implements SugangScheduleRepository {

    private final SugangScheduleJpaRepository sugangScheduleJpaRepository;

    public SugangScheduleRepositoryImpl(SugangScheduleJpaRepository sugangScheduleJpaRepository) {
        this.sugangScheduleJpaRepository = sugangScheduleJpaRepository;
    }

    public Optional<SugangSchedule> findBySugang_SugangIdAndClassDate(Long sugang, String classDate) {
        Optional<SugangScheduleEntity> sugangScheduleEntityOptional = sugangScheduleJpaRepository.findBySugang_SugangIdAndClassDate(sugang,classDate);

        SugangScheduleEntity sugangScheduleEntity = sugangScheduleEntityOptional.get();

        return Optional.of(SugangScheduleMapper.sugangScheduleToPojo(sugangScheduleEntity));
    };

    public <S extends SugangSchedule> S save(S item) {
        SugangScheduleEntity sugangScheduleEntity = sugangScheduleJpaRepository.save(SugangScheduleMapper.sugangScheduleToEntity(item));
        return (S) SugangScheduleMapper.sugangScheduleToPojo(sugangScheduleEntity);
    }

    public List<SugangSchedule> findAllByClassDate(String classDate) {
        List<SugangScheduleEntity> sugangScheduleEntities = sugangScheduleJpaRepository.findAllByClassDate(classDate);
        return SugangScheduleMapper.sugangScheduleToPojoList(sugangScheduleEntities);
    }

}
