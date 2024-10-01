package io.hhplus.cleancode.infrastructure.entity;


import jakarta.persistence.*;

@Entity
@Table(name="SugangHistory", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"classId", "studentId", "classDate"})
})
public class SugangHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

//    @ManyToOne
//    @JoinColumn(name = "sugang_id", referencedColumnName = "sugang_id")
////    @Column(nullable = false)
//    private Sugang sugang;
//
//    @ManyToOne
//    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
////    @Column(nullable = false)
//    private Student student;

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
}
