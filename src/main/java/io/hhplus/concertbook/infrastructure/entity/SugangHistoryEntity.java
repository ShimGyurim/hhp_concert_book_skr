package io.hhplus.concertbook.infrastructure.entity;


import jakarta.persistence.*;

@Entity
@Table(name="SugangHistory", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sugang_id", "studentId", "classDate"})
})
public class SugangHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "sugangschedule_id", referencedColumnName = "sugangschedule_id")
//    @Column(nullable = false)
    private SugangScheduleEntity sugangSchedule;

    @ManyToOne
    @JoinColumn(name = "sugang_id", referencedColumnName = "sugang_id")
//    @Column(nullable = false)
    private SugangEntity sugang;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
//    @Column(nullable = false)
    private StudentEntity student;

    @Column(nullable = false)
    private String classDate;

    public void setSugangSchedule(SugangScheduleEntity sugangSchedule) {
        this.sugangSchedule = sugangSchedule;
    }

    public void setSugang(SugangEntity sugang) {
        this.sugang = sugang;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public SugangScheduleEntity getSugangSchedule() {
        return sugangSchedule;
    }

    public SugangEntity getSugang() {
        return sugang;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public String getClassDate() {
        return classDate;
    }

    @Override
    public String toString() {
        return "SugangHistory{" +
                "historyId=" + historyId +
                ", sugangSchedule=" + sugangSchedule +
                ", sugang=" + sugang +
                ", student=" + student +
                ", classDate='" + classDate + '\'' +
                '}';
    }
}
