package io.hhplus.concertbook.infrastructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name="STUDENT")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id")
    private Long studentId;

//    private String name;

    public Long getStudentId() {
        return studentId;
    }

    public StudentEntity() {
    }

    public StudentEntity(Long studentId) {
        this.studentId = studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                '}';
    }
}
