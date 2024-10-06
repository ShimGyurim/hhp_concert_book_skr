package io.hhplus.concertbook.domain.entity;

public class Student {

    private Long studentId;


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
