package br.com.pehenmo.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class JobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        File file = new File("src\\main\\resources\\csv\\output.csv");
        if(null!= file && file.exists()){
            file.delete();
        }

    }
}
