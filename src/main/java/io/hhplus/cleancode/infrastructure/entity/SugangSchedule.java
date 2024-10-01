package io.hhplus.cleancode.infrastructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name="SugangSchedule", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sugang_id", "student_id", "classdate"})
})
public class SugangSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sugangschedule_id")
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "sugang_id", referencedColumnName = "sugang_id")
//    @Column(nullable = false)
    private Sugang sugang;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
//    @Column(nullable = false)
    private Student student;

    @Column(nullable = false, name="classdate")
    private String classDate;

    @Column(nullable = true)
    private Long availNum;
}
