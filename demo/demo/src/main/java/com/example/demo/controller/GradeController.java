package com.example.demo.controller;

import com.example.demo.dto.GradeDTO;
import com.example.demo.dto.MessageDTO;
import com.example.demo.exception.AlreadyGradedException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GradeController {
    @Autowired
    GradeService gradeService;

    @PostMapping(value="api/newGrade")
    public ResponseEntity newGrade(@RequestBody GradeDTO grade) throws EmailNotFoundException, AlreadyGradedException {
        gradeService.addGrade(grade);
        //messagesService.notifySupport();
        return new ResponseEntity("",HttpStatus.OK);
    }
    @GetMapping(value = "/api/getGrades")
    public ResponseEntity getDriver(@RequestParam Long driveID) {
        return new ResponseEntity(gradeService.getGradesForDrive(driveID), HttpStatus.OK);

    }
}
