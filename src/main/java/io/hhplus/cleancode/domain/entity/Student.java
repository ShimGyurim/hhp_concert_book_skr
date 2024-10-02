package io.hhplus.cleancode.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name="STUDENT")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id")
    private Long studentId;

//    private String name;

    public Long getStudentId() {
        return studentId;
    }

    public Student() {
    }

    public Student(Long studentId) {
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
