package io.hhplus.concertbook.domain.entity;


public class SugangHistory {

    private Long historyId;

    private SugangSchedule sugangSchedule;

    private Sugang sugang;

    private Student student;

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
