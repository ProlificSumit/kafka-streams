package com.learn.controller;

import com.learn.EmployeeExpenseRequest;
import com.learn.service.ProducerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @PostMapping("/create")
    public String create(@RequestBody EmployeeExpenseRequest employeeExpenseRequest) {
        log.info("Controller create {} ", employeeExpenseRequest);
        producerService.sendMessage(employeeExpenseRequest);
        return "producer created " + employeeExpenseRequest;
    }
}
