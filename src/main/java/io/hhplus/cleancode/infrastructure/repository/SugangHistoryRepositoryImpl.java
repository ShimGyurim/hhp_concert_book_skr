package io.hhplus.cleancode.infrastructure.repository;

//import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.cleancode.domain.entity.SugangHistory;
import io.hhplus.cleancode.domain.repository.SugangHistoryRepository;
import io.hhplus.cleancode.infrastructure.entity.SugangHistoryEntity;
import io.hhplus.cleancode.infrastructure.mapper.SugangHistoryMapper;

import java.util.List;

public class SugangHistoryRepositoryImpl implements SugangHistoryRepository {

    private final SugangHistoryJpaRepository sugangHistoryJpaRepository;

    public SugangHistoryRepositoryImpl(SugangHistoryJpaRepository sugangHistoryJpaRepository){
        this.sugangHistoryJpaRepository=sugangHistoryJpaRepository;
    }
    public <S extends SugangHistory> S save(S pojo) {
        SugangHistoryEntity sugangHistoryEntity = sugangHistoryJpaRepository.save(SugangHistoryMapper.sugangHistoryToEntity(pojo));
        return (S) SugangHistoryMapper.sugangHistoryToPojo(sugangHistoryEntity);
    }

    public List<SugangHistory> findAllByStudent_StudentId(Long studentId) {
        return SugangHistoryMapper.sugangHistoryToPojoList(
                sugangHistoryJpaRepository.findAllByStudent_StudentId(studentId));
    }
}
