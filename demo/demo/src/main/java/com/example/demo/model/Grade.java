package com.example.demo.model;

import com.example.demo.dto.GradeDTO;

import javax.persistence.*;

@Table
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int gradeCar;

    @Column
    private int gradeDriver;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "drive")
    private Drive drive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rater")
    private User rater;

    @Column
    private GradeStatus gradeStatus;

    public Grade() {
    }

    public Grade(Long id, int gradeCar, int gradeDriver, String comment, Drive drive, User rater, GradeStatus gradeStatus) {
        this.id = id;
        this.gradeCar = gradeCar;
        this.gradeDriver = gradeDriver;
        this.comment = comment;
        this.drive = drive;
        this.rater = rater;
        this.gradeStatus = gradeStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGradeCar() {
        return gradeCar;
    }

    public void setGradeCar(int gradeCar) {
        this.gradeCar = gradeCar;
    }

    public int getGradeDriver() {
        return gradeDriver;
    }

    public void setGradeDriver(int gradeDriver) {
        this.gradeDriver = gradeDriver;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    public User getRater() {
        return rater;
    }

    public void setRater(User rater) {
        this.rater = rater;
    }

    public GradeStatus getGradeStatus() {
        return gradeStatus;
    }

    public void setGradeStatus(GradeStatus gradeStatus) {
        this.gradeStatus = gradeStatus;
    }
}
