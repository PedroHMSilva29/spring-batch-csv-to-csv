package br.com.pehenmo.batch.config;

import br.com.pehenmo.batch.entity.Student;
import br.com.pehenmo.batch.listener.JobResultListener;
import br.com.pehenmo.batch.listener.StepOneResultListener;
import br.com.pehenmo.batch.listener.StepTwoResultListener;
import br.com.pehenmo.batch.processor.StudentProcessor;
import br.com.pehenmo.batch.util.BatchProperties;
import br.com.pehenmo.batch.writer.AWSRequestWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfigProcessing {

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    BatchProperties batchProperties;

    @Value("${userBucket.path}")
    private String userBucketPath;

    @Autowired
    JobResultListener jobResultListener;

    @Bean
    public FlatFileItemReader<Student> reader() {

        FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
        reader.setResource(new FileSystemResource(batchProperties.getInputCsvFileSource()));
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());

        return reader;
    }

    private LineMapper lineMapper() {
        DefaultLineMapper lineMapper = new DefaultLineMapper();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(Student.filds());

        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<Student>();
        fieldSetMapper.setTargetType(Student.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public FlatFileItemWriter<Student> fileWriter() throws Exception {

        return new FlatFileItemWriterBuilder<Student>()
                .name("csv-writer")
                .append(true)
                .resource(new FileSystemResource(batchProperties.getOutputCsvFileSource()))
                .lineAggregator(lineAggregator())
                .build();
    }

    private LineAggregator<Student> lineAggregator() {

        DelimitedLineAggregator<Student> lineAggregator = new DelimitedLineAggregator<Student>();

        BeanWrapperFieldExtractor<Student> fieldExtractor = new BeanWrapperFieldExtractor<Student>();
        fieldExtractor.setNames(Student.filds());

        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return lineAggregator;

    }

    @Bean
    public ItemWriter<Student> writeBucketS3() throws Exception {
        return new AWSRequestWriter();
    }

    @Bean
    public StudentProcessor processor() {
        return new StudentProcessor();
    }

    @Bean
    public Step csvStep() throws Exception {
        return stepBuilderFactory
                .get("csv-step").<Student, Student>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(fileWriter())
                .listener(new StepOneResultListener())
                .build();
    }


    @Bean
    public Step awsStep() throws Exception {
        return stepBuilderFactory
                .get("aws-step").<Student, Student>chunk(5)
                .reader(reader())
                .writer(writeBucketS3())
                .listener(new StepTwoResultListener())
                .build();
    }

    @Bean
    public Job readCSVFilesJob() throws Exception {
        return jobBuilderFactory
                .get("main-job")
                .incrementer(new RunIdIncrementer())
                .listener(jobResultListener)
                .start(csvStep())
                .next(awsStep())
                .build();
    }

}
