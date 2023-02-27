package az.traveltime.traveltimeweb.controller;

import az.traveltime.traveltimeweb.service.AnswerDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelTime")
@RequiredArgsConstructor
public class AnswerController {
    @Autowired
    AnswerDtoService answerDtoService;
    @CrossOrigin
    @GetMapping("/answer")
    public ResponseEntity<List> getAnswer() {
        return ResponseEntity.ok(AnswerDtoService.list);
    }

}
