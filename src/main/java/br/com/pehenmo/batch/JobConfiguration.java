package br.com.pehenmo.batch;

import br.com.pehenmo.batch.entity.Student;
import br.com.pehenmo.batch.processor.StudentProcessor;
import br.com.pehenmo.batch.reader.FileRequestReader;
import br.com.pehenmo.batch.writer.AWSRequestWriter;
import br.com.pehenmo.batch.writer.FileRequestWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    FileRequestReader fileReader;

    @Autowired
    FileRequestWriter fileWriter;

    @Autowired
    AWSRequestWriter awsWriter;

    @Bean
    @StepScope
    public MultiResourceItemReader<Student> fileReader() {
        return fileReader.read();
    }

    @Bean
    @StepScope
    public StudentProcessor processor() {
        return new StudentProcessor();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Student> fileWriter() {
        return fileWriter.write();
    }

    @Bean
    @StepScope
    public Tasklet s3BucketWriter() {
        return awsWriter;
    }

    /**
     @Bean
     @StepScope
     public CompositeItemWriter<Student> compositeItemWriter(){
     CompositeItemWriter writer = new CompositeItemWriter();
     writer.setDelegates(Arrays.asList(fileWriter(), s3BucketWriter()));
     return writer;
     }**/

    @Bean
    public Step fileStep() {
        return stepBuilderFactory
                .get("file-step").<Student, Student>chunk(2)
                .reader(fileReader())
                .processor(processor())
                .writer(fileWriter())
                .build();
    }

    @Bean
    public Step s3Step() {
        return stepBuilderFactory
                .get("s3-step")
                .tasklet(s3BucketWriter())
                .build();
    }

    @Bean
    public Job mainJob() {
        return jobBuilderFactory
                .get("main-job")
                .incrementer(new RunIdIncrementer())
                .start(fileStep())
                .next(s3Step())
                .build();
    }

}
