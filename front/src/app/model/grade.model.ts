import {Status, User} from "./user.model";
import {Drive} from "./drive.model";

export enum GradeStatus {
  NOT_GRADED = 'NOT_GRADED',
  GRADED = 'GRADED',
  WAITING_FOR_GRADE = 'WAITING_FOR_GRADE',
}


interface GradeInterface{
  gradeCar: number;
  gradeDriver: number;
  comment: string;
  drive: Drive;
  rater: User;
  gradeStatus: GradeStatus;

}

export class Grade implements GradeInterface{
  public gradeCar: number;
  public gradeDriver: number;
  public comment: string;
  public drive: Drive;
  public rater: User;
  public gradeStatus: GradeStatus;

  constructor(gradeInterface: GradeInterface){
    this.gradeCar = gradeInterface.gradeCar;
    this.gradeDriver = gradeInterface.gradeDriver;
    this.comment = gradeInterface.comment;
    this.drive = gradeInterface.drive;
    this.rater = gradeInterface.rater;
    this.gradeStatus = gradeInterface.gradeStatus;
  }
}
