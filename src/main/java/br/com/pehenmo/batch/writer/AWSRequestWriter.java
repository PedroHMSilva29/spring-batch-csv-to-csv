package br.com.pehenmo.batch.writer;

import br.com.pehenmo.aws.config.AWSS3Response;
import br.com.pehenmo.aws.factory.AWSS3ClientFactory;
import br.com.pehenmo.aws.writer.WriteBucketS3;
import br.com.pehenmo.batch.entity.Student;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@StepScope
public class AWSRequestWriter implements ItemWriter<Student> {

    @Autowired
    private AWSS3ClientFactory factory;

    @Autowired
    private AWSS3Response response;

    private static final String PATH = "./src/main/resources/csv/output.csv";

    @Override
    public void write(List<? extends Student> list) throws Exception {

        // escreve no bucket S3
        WriteBucketS3.putObject(factory.generate(), response.getKeyName(), response.getBucketName(), PATH);

    }
}
