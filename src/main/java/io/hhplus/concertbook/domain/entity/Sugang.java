package io.hhplus.concertbook.domain.entity;

public class Sugang {


    private Long sugangId;

    private String className;

    public Sugang() {
    }

    public Sugang(Long sugangId) {
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
