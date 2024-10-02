package io.hhplus.cleancode.domain.service;

import io.hhplus.cleancode.domain.dto.SugangDto;
import io.hhplus.cleancode.domain.mapper.SugangDtoMapper;
import io.hhplus.cleancode.infrastructure.entity.StudentEntity;
import io.hhplus.cleancode.infrastructure.entity.SugangEntity;
import io.hhplus.cleancode.infrastructure.entity.SugangHistoryEntity;
import io.hhplus.cleancode.infrastructure.entity.SugangScheduleEntity;
import io.hhplus.cleancode.infrastructure.repository.StudentJpaRepository;
import io.hhplus.cleancode.infrastructure.repository.SugangHistoryJpaRepository;
import io.hhplus.cleancode.infrastructure.repository.SugangJpaRepository;
import io.hhplus.cleancode.infrastructure.repository.SugangScheduleJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SugangService {

    @Autowired
    SugangJpaRepository sugangRepository;

    @Autowired
    StudentJpaRepository studentRepository;

    @Autowired
    SugangScheduleJpaRepository sugangScheduleRepository;

    @Autowired
    SugangHistoryJpaRepository sugangHistoryRepository;

    private static final Logger log = LoggerFactory.getLogger(SugangService.class);

    @Transactional
    public String insert (SugangDto sugangInsertDto) {

        Optional<SugangEntity> sugangOptional = sugangRepository.findById(sugangInsertDto.getSugangId());
        SugangEntity sugang;
        if(sugangOptional.isEmpty()) {
            sugang = new SugangEntity();
            sugang.setSugangId(sugangInsertDto.getSugangId());
            sugang.setClassName(sugangInsertDto.getClassName());
            sugang = sugangRepository.save(sugang);
        }else{
            sugang = sugangOptional.get();
        }

        Optional<StudentEntity> studentOptional = studentRepository.findById(sugangInsertDto.getStudentId());
        StudentEntity student;
        if(studentOptional.isEmpty()) {
            student = new StudentEntity();
            student.setStudentId(sugangInsertDto.getStudentId());
            student = studentRepository.save(student);


        }else {
            student = studentOptional.get();
        }

        Optional<SugangScheduleEntity> sugangScheduleOptional = sugangScheduleRepository.findBySugang_SugangIdAndClassDate(sugangInsertDto.getSugangId(), sugangInsertDto.getClassDate());
        if(sugangScheduleOptional.isEmpty()) {
            SugangScheduleEntity sugangSchedule = new SugangScheduleEntity();

//            Student student = new Student();
//            student.setStudentId(sugangInsertDto.getStudentId());
//            sugangSchedule.setStudent(student);

//            Sugang sugang = new Sugang();
//            sugang.setSugangId(sugangInsertDto.getSugangId());
            sugangSchedule.setSugang(sugang);

            log.error("학생 " +student.toString());
            log.error("수강 " +sugang.toString());

            sugangSchedule.setAvailNum(sugangInsertDto.getAvailNum());
            sugangSchedule.setClassDate(sugangInsertDto.getClassDate());


            log.error("로그 " +sugangSchedule.toString());
            sugangScheduleRepository.save(sugangSchedule);
        }

        return "success";
    }

    @Transactional
    public String apply (SugangDto sugangInsertDto) {
        Optional<SugangScheduleEntity> sugangScheduleOptional = Optional.ofNullable(sugangScheduleRepository.findBySugang_SugangIdAndClassDate(sugangInsertDto.getSugangId(), sugangInsertDto.getClassDate())
                .orElseThrow(() -> new DataAccessResourceFailureException("해당 수강 일정을 찾을 수 없습니다.")));
        SugangScheduleEntity sugangSchedule = sugangScheduleOptional.get();


        // 낙관적 락: 버전 확인 후 변경
        if (sugangSchedule.getAvailNum() > 0) {
            sugangSchedule.setAvailNum(sugangSchedule.getAvailNum() - 1);

            Long scheduleId = sugangSchedule.getScheduleId();

            SugangHistoryEntity sugangHistory = new SugangHistoryEntity();

            sugangHistory.setSugangSchedule(sugangSchedule);
            sugangHistory.setSugang(sugangSchedule.getSugang());
            sugangHistory.setStudent(new StudentEntity(sugangInsertDto.getStudentId()));
            sugangHistory.setClassDate(sugangSchedule.getClassDate());

            try {
                log.error("로그 " +sugangHistory.toString());
                sugangScheduleRepository.save(sugangSchedule);
                sugangHistoryRepository.save(sugangHistory); //수강신청내역에 저장
//            } catch (OptimisticLockingFailureException e) {
//                // 동시성 충돌 발생 시 처리 로직
//                return "fail";
            }catch (Exception e){
                e.getStackTrace();
            }
            return "success";
        } else {
            return "fail";
        }
    }

    public List<SugangDto> getClassAvail(SugangDto sugangInsertDto) {
        //schedule 에서 findByClassDate
        List<SugangScheduleEntity> sugangScheduleList = sugangScheduleRepository.findAllByClassDate(sugangInsertDto.getClassDate());

        return SugangDtoMapper.toSugangDtoMapper(sugangScheduleList);
    }

    public List<SugangDto> getClassApplyHistory(SugangDto sugangInsertDto) {
        //schedule 에서 findByClassDate

        log.error("로그:"+sugangInsertDto.getStudentId());
        List<SugangHistoryEntity> sugangScheduleList = sugangHistoryRepository.findAllByStudent_StudentId(sugangInsertDto.getStudentId());


        log.error("로그:"+sugangScheduleList.stream()
                .map(SugangHistoryEntity::toString)
                .collect(Collectors.toList()));
        return SugangDtoMapper.historyToSugangDtoMapper(sugangScheduleList);
    }

}


