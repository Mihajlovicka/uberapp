package com.example.demo.dto;

import com.example.demo.model.Drive;
import com.example.demo.model.GradeStatus;
import com.example.demo.model.User;


public class GradeDTO {


     private int gradeCar;

     private int gradeDriver;

     private String comment;

     private Drive drive;

     private UserDTO rater;

     private GradeStatus gradeStatus;

 public GradeDTO() {
 }

 public GradeDTO(int gradeCar, int gradeDriver, String comment, Drive drive, UserDTO rater, GradeStatus gradeStatus) {
  this.gradeCar = gradeCar;
  this.gradeDriver = gradeDriver;
  this.comment = comment;
  this.drive = drive;
  this.rater = rater;
  this.gradeStatus = gradeStatus;
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

 public UserDTO getRater() {
  return rater;
 }

 public void setRater(UserDTO rater) {
  this.rater = rater;
 }

 public GradeStatus getGradeStatus() {
  return gradeStatus;
 }

 public void setGradeStatus(GradeStatus gradeStatus) {
  this.gradeStatus = gradeStatus;
 }
}
