package br.com.pehenmo.batch.reader;

import br.com.pehenmo.batch.entity.Student;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class FileRequestReader{

    @Value("csv/input*.csv")
    private Resource[] inputResources;

    public MultiResourceItemReader<Student> read() {
        MultiResourceItemReader itemReader = new MultiResourceItemReader<Student>();
        itemReader.setResources(inputResources);
        itemReader.setDelegate(reader());
        return itemReader;
    }

    public FlatFileItemReader<Student> reader() {
        FlatFileItemReader itemReader = new FlatFileItemReaderBuilder<Student>()
                .name("csv-reader")
                .linesToSkip(1)
                .lineMapper(lineMapper())
                .build();

        return itemReader;
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
}
