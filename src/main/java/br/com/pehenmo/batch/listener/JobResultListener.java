package br.com.pehenmo.batch.listener;

import br.com.pehenmo.batch.util.BatchProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class JobResultListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobResultListener.class);

    @Autowired
    BatchProperties batchProperties;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("Called beforeJob()");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        File output = new File(batchProperties.getOutputCsvFileSource());
        output.delete();

        if (jobExecution.getStatus() == BatchStatus.COMPLETED ) {
            LOGGER.info("Job Status: COMPLETED");
        }
        else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            LOGGER.error("Job Status: FAILED");
        }

    }
}
