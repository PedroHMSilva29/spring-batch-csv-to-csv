package br.com.pehenmo.batch;

import br.com.pehenmo.batch.entity.ResultCSV;
import br.com.pehenmo.batch.entity.Student;
import br.com.pehenmo.batch.entity.Worker;
import br.com.pehenmo.batch.listener.JobListener;
import br.com.pehenmo.batch.processor.StudentProcessor;
import br.com.pehenmo.batch.processor.WorkerProcessor;
import br.com.pehenmo.batch.reader.FileRequestStudantReader;
import br.com.pehenmo.batch.reader.FileRequestWorkerReader;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    JobListener jobListener;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    FileRequestStudantReader fileStudantReader;

    @Autowired
    FileRequestWorkerReader fileWorkerReader;

    @Autowired
    FileRequestWriter fileWriter;

    @Autowired
    AWSRequestWriter awsWriter;

    @Bean
    @StepScope
    public FlatFileItemReader<Student> fileReaderStudent() {
        return fileStudantReader.reader();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Worker> fileReaderWorker() {
        return fileWorkerReader.reader();
    }

    @Bean
    @StepScope
    public StudentProcessor processorStudent() {
        return new StudentProcessor();
    }

    @Bean
    @StepScope
    public WorkerProcessor processorWorker() {
        return new WorkerProcessor();
    }


    @Bean
    @StepScope
    public FlatFileItemWriter<ResultCSV> fileWriter() {
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
    public Step fileWorkerStep() {
        return stepBuilderFactory
                .get("file-worker-step").<Worker, ResultCSV>chunk(2)
                .reader(fileReaderWorker())
                .processor(processorWorker())
                .writer(fileWriter())
                .build();
    }

    @Bean
    public Step fileStudentStep() {
        return stepBuilderFactory
                .get("file-student-step").<Student, ResultCSV>chunk(2)
                .reader(fileReaderStudent())
                .processor(processorStudent())
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
                .start(fileStudentStep())
                .next(fileWorkerStep())
                .next(s3Step())
                .listener(jobListener)
                .build();
    }

}
