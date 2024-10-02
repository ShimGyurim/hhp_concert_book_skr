package io.hhplus.cleancode.infrastructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name="SUGANG")
public class SugangEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sugang_id")
    private Long sugangId;

    private String className;

    public SugangEntity() {
    }

    public SugangEntity(Long sugangId) {
        this.sugangId = sugangId;
    }

    public Long getSugangId() {
        return sugangId;
    }

    public void setSugangId(Long sugangId) {
        this.sugangId = sugangId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }



    @Override
    public String toString() {
        return "Sugang{" +
                "sugangId=" + sugangId +
                ", className='" + className + '\'' +
                '}';
    }
}
