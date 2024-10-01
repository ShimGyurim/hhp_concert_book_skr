package io.hhplus.cleancode.domain.service;

import io.hhplus.cleancode.application.dto.SugangInsertDto;
import io.hhplus.cleancode.infrastructure.entity.Student;
import io.hhplus.cleancode.infrastructure.entity.Sugang;
import io.hhplus.cleancode.infrastructure.entity.SugangHistory;
import io.hhplus.cleancode.infrastructure.entity.SugangSchedule;
import io.hhplus.cleancode.infrastructure.repository.StudentRepository;
import io.hhplus.cleancode.infrastructure.repository.SugangHistoryRepository;
import io.hhplus.cleancode.infrastructure.repository.SugangRepository;
import io.hhplus.cleancode.infrastructure.repository.SugangScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SugangService {

    @Autowired
    SugangRepository sugangRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SugangScheduleRepository sugangScheduleRepository;

    @Autowired
    SugangHistoryRepository sugangHistoryRepository;

    private static final Logger log = LoggerFactory.getLogger(SugangService.class);

    @Transactional
    public String insert (SugangInsertDto sugangInsertDto) {

        Optional<Sugang> sugangOptional = sugangRepository.findById(sugangInsertDto.getSugangId());
        Sugang sugang;
        if(sugangOptional.isEmpty()) {
            sugang = new Sugang();
            sugang.setSugangId(sugangInsertDto.getSugangId());
            sugang.setClassName(sugangInsertDto.getClassName());
            sugang = sugangRepository.save(sugang);
        }else{
            sugang = sugangOptional.get();
        }

        Optional<Student> studentOptional = studentRepository.findById(sugangInsertDto.getStudentId());
        Student student;
        if(studentOptional.isEmpty()) {
            student = new Student();
            student.setStudentId(sugangInsertDto.getStudentId());
            student = studentRepository.save(student);


        }else {
            student = studentOptional.get();
        }

        Optional<SugangSchedule> sugangScheduleOptional = sugangScheduleRepository.findByStudent_StudentIdAndSugang_SugangIdAndClassDate(sugangInsertDto.getStudentId(), sugangInsertDto.getSugangId(), sugangInsertDto.getClassDate());
        if(sugangScheduleOptional.isEmpty()) {
            SugangSchedule sugangSchedule = new SugangSchedule();

//            Student student = new Student();
//            student.setStudentId(sugangInsertDto.getStudentId());
            sugangSchedule.setStudent(student);

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
    public String apply (SugangInsertDto sugangInsertDto) {
        Optional<SugangSchedule> sugangScheduleOptional = Optional.ofNullable(sugangScheduleRepository.findByStudent_StudentIdAndSugang_SugangIdAndClassDate(sugangInsertDto.getStudentId(), sugangInsertDto.getSugangId(), sugangInsertDto.getClassDate())
                .orElseThrow(() -> new DataAccessResourceFailureException("해당 수강 일정을 찾을 수 없습니다.")));
        SugangSchedule sugangSchedule = sugangScheduleOptional.get();


        // 낙관적 락: 버전 확인 후 변경
        if (sugangSchedule.getAvailNum() > 0) {
            sugangSchedule.setAvailNum(sugangSchedule.getAvailNum() - 1);

            Long scheduleId = sugangSchedule.getScheduleId();

            SugangHistory sugangHistory = new SugangHistory();

            sugangHistory.setSugangSchedule(sugangSchedule);
            sugangHistory.setSugang(sugangSchedule.getSugang());
            sugangHistory.setStudent(sugangSchedule.getStudent());
            sugangHistory.setClassDate(sugangSchedule.getClassDate());

            try {
                log.error("로그 " +sugangHistory.toString());
                sugangScheduleRepository.save(sugangSchedule);
                sugangHistoryRepository.save(sugangHistory); //수강신청내역에 저장
//            } catch (OptimisticLockingFailureException e) {
//                // 동시성 충돌 발생 시 처리 로직
//                return "fail";
            }
            return "success";
        } else {
            return "fail";
        }
    }


}


