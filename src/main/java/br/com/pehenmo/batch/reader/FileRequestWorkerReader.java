package br.com.pehenmo.batch.reader;

import br.com.pehenmo.batch.entity.Worker;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class FileRequestWorkerReader {

    private Resource outputResource = new FileSystemResource("src\\main\\resources\\csv\\input2.csv");

    public FlatFileItemReader<Worker> reader() {
        FlatFileItemReader itemReader = new FlatFileItemReaderBuilder<Worker>()
                .name("csv-reader")
                .resource(outputResource)
                .linesToSkip(1)
                .lineMapper(lineMapper())
                .build();

        return itemReader;
    }

    private LineMapper lineMapper() {

        DefaultLineMapper lineMapper = new DefaultLineMapper();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(Worker.filds());

        BeanWrapperFieldSetMapper<Worker> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Worker.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
