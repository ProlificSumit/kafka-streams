package com.learn.service;

import com.learn.EmployeeExpenseRequest;
import com.learn.schema.record.EmployeeExpenseDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Service
public class ProducerService {

    @Value("${usage.topic.name}")
    private String topicName;
    @Autowired
    private KafkaTemplate<String, EmployeeExpenseDetails> kafkaTemplate;
    private final AtomicInteger counter = new AtomicInteger();

    public ProducerService() {
    }

    ProducerService(String topicName) {
        this.topicName = topicName;
    }

    public void sendMessage(EmployeeExpenseRequest employeeExpenseRequest) {
        log.info("request : {} ", employeeExpenseRequest);
        EmployeeExpenseDetails employeeExpenseDetails = new EmployeeExpenseDetails();
        employeeExpenseDetails.setEmployeeId(employeeExpenseRequest.getEmployeeId());
        employeeExpenseDetails.setEmployeeName(employeeExpenseRequest.getEmployeeName());
        employeeExpenseDetails.setDesignation(employeeExpenseRequest.getDesignation());
        employeeExpenseDetails.setJoiningDate(employeeExpenseRequest.getJoiningDate());
        sendRecord(employeeExpenseDetails);
        log.info("Message {} processed ", employeeExpenseDetails);
    }

    private void sendRecord(EmployeeExpenseDetails employeeExpenseDetails) {
        //final ProducerRecord<String, EmployeeExpenseDetails> record = createRecord(employeeExpenseDetails);
        log.info("sendRecord starts : record {} ", employeeExpenseDetails);
        CompletableFuture<SendResult<String, EmployeeExpenseDetails>> future;
        for (int i = 0; i < 100; i++) {
            future = kafkaTemplate.send(topicName,
                    createKey(employeeExpenseDetails), employeeExpenseDetails);

//        CompletableFuture<SendResult<String, EmployeeExpenseDetails>> future = kafkaTemplate.send(topicName,
//                createKey(employeeExpenseDetails), employeeExpenseDetails);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    handleSuccess(employeeExpenseDetails);
                } else {
                    handleFailure(employeeExpenseDetails, ex);
                }
            });
        }
    }

    private void handleSuccess(EmployeeExpenseDetails employeeExpenseDetails) {
        log.info("Message processed successfully , data: {}", employeeExpenseDetails);
    }

    private void handleFailure(EmployeeExpenseDetails employeeExpenseDetails, Throwable exception) {
        log.error("Error occurred while processing data : {} , exception : {} ", employeeExpenseDetails, exception);
    }

    private String createKey(EmployeeExpenseDetails employeeExpenseDetails) {
        log.info("Key {}", employeeExpenseDetails.getEmployeeId() + " - " + employeeExpenseDetails.getJoiningDate());
        return employeeExpenseDetails.getEmployeeId() + " - " + employeeExpenseDetails.getJoiningDate();
    }
}
