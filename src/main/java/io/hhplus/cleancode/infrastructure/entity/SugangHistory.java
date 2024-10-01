package io.hhplus.cleancode.infrastructure.entity;


import jakarta.persistence.*;

@Entity
@Table(name="SugangHistory", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sugang_id", "studentId", "classDate"})
})
public class SugangHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "sugangschedule_id", referencedColumnName = "sugangschedule_id")
//    @Column(nullable = false)
    private SugangSchedule sugangSchedule;

    @ManyToOne
    @JoinColumn(name = "sugang_id", referencedColumnName = "sugang_id")
//    @Column(nullable = false)
    private Sugang sugang;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
//    @Column(nullable = false)
    private Student student;

    @Column(nullable = false)
    private String classDate;

    public void setSugangSchedule(SugangSchedule sugangSchedule) {
        this.sugangSchedule = sugangSchedule;
    }

    public void setSugang(Sugang sugang) {
        this.sugang = sugang;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public SugangSchedule getSugangSchedule() {
        return sugangSchedule;
    }

    public Sugang getSugang() {
        return sugang;
    }

    public Student getStudent() {
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
