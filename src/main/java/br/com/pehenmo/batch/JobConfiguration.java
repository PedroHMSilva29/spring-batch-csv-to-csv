package br.com.pehenmo.batch;

import br.com.pehenmo.batch.listener.JobListener;
import br.com.pehenmo.batch.processor.DatabaseProcessor;
import br.com.pehenmo.batch.processor.StudentProcessor;
import br.com.pehenmo.batch.processor.WorkerProcessor;
import br.com.pehenmo.batch.reader.FileRequestStudantReader;
import br.com.pehenmo.batch.reader.FileRequestWorkerReader;
import br.com.pehenmo.batch.reader.ResultCSVRequestReader;
import br.com.pehenmo.batch.writer.AWSRequestWriter;
import br.com.pehenmo.batch.writer.DatabaseRequestWriter;
import br.com.pehenmo.batch.writer.FileRequestWriter;
import br.com.pehenmo.postgres.entity.StudanteEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableBatchProcessing
@EnableJpaRepositories("br.com.pehenmo.postgres.repository")
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
    ResultCSVRequestReader resultCSVRequestReader;

    @Autowired
    FileRequestWriter fileWriter;

    @Autowired
    DatabaseRequestWriter databaseWriter;

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
    public FlatFileItemReader<ResultCSV> fileReaderResultCSV() {
        return resultCSVRequestReader.reader();
    }

    @Bean
    @StepScope
    public StudentProcessor processorStudent() {
        return new StudentProcessor();
    }

    @Bean
    @StepScope
    public DatabaseProcessor processorDatabase() {
        return new DatabaseProcessor();
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
    public ItemWriter<StudanteEntity> databaseWriter() {
        return databaseWriter;
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
    public Step fileDatabaseStep() {
        return stepBuilderFactory
                .get("file-database-step").<ResultCSV, StudanteEntity>chunk(2)
                .reader(fileReaderResultCSV())
                .processor(processorDatabase())
                .writer(databaseWriter())
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
                .next(fileDatabaseStep())
                .listener(jobListener)
                .build();
    }

}
