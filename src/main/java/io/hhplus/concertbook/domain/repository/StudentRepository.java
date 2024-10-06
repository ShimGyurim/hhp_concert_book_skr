package io.hhplus.concertbook.domain.repository;

import io.hhplus.concertbook.domain.entity.Student;

public interface StudentRepository  {

    <S extends Student> S save(S pojo);
}
