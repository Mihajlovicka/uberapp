package com.example.demo.service;

import com.example.demo.dto.GradeDTO;
import com.example.demo.exception.AlreadyGradedException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.model.Grade;
import com.example.demo.repository.GradesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GradeService {
    @Autowired
    private GradesRepository gradesRepository;
    @Autowired
    private UserService userService;


    public List<Grade> getGradesForDrive(Long driveID){
        List<Grade> grades = new ArrayList<Grade>();
        for(Grade grade : gradesRepository.findAll()){
            if(grade.getDrive().getId() == driveID){
                grades.add(grade);
            }
        }
        return grades;
    }

    public void addGrade(GradeDTO gradeDTO) throws AlreadyGradedException, EmailNotFoundException {
        Grade grade = new Grade();
        grade.setGradeCar(gradeDTO.getGradeCar());
        grade.setGradeDriver(gradeDTO.getGradeDriver());
        grade.setComment(gradeDTO.getComment());
        grade.setDrive(gradeDTO.getDrive());
        grade.setGradeStatus(gradeDTO.getGradeStatus());
        grade.setRater(userService.getByEmail(gradeDTO.getRater().getEmail()));

        boolean graded = false;

        for(Grade g : getGradesForDrive(grade.getDrive().getId())){
            if(g.getRater().getEmail().equals(grade.getRater().getEmail())){
                graded = true;
            }
        }
        if(!graded){
            gradesRepository.save(grade);
        }
        else{
            throw new AlreadyGradedException("User already graded this drive.");
        }
    }

}
