package br.com.pehenmo.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepTwoResultListener implements StepExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobResultListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.info("Called beforeJob()");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("Step finished: Arquivo enviado ao bucket S3");
        return stepExecution.getExitStatus();
    }
}
