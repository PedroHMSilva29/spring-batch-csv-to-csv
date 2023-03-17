package br.com.pehenmo.batch.writer;

import br.com.pehenmo.aws.config.AWSS3Response;
import br.com.pehenmo.aws.factory.AWSS3ClientFactory;
import br.com.pehenmo.aws.writer.WriteBucketS3;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AWSRequestWriter implements Tasklet {

    @Autowired
    private AWSS3ClientFactory factory;

    @Autowired
    private AWSS3Response response;

    private static final String PATH = "./src/main/resources/csv/output.csv";

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        // escreve no bucket S3
        WriteBucketS3.putObject(factory.generate(), response.getKeyName(), response.getBucketName(), PATH);
        return RepeatStatus.FINISHED;
    }
}
