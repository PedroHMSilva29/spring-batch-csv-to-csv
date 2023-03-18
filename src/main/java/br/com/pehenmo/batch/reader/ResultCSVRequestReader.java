package br.com.pehenmo.batch.reader;

import br.com.pehenmo.batch.ResultCSV;
import br.com.pehenmo.batch.Worker;
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
public class ResultCSVRequestReader {

    private Resource outputResource = new FileSystemResource("src\\main\\resources\\csv\\output.csv");

    public FlatFileItemReader<ResultCSV> reader() {
        FlatFileItemReader itemReader = new FlatFileItemReaderBuilder<ResultCSV>()
                .name("csv-reader")
                .resource(outputResource)
                .lineMapper(lineMapper())
                .build();

        return itemReader;
    }

    private LineMapper lineMapper() {

        DefaultLineMapper lineMapper = new DefaultLineMapper();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(ResultCSV.filds());

        BeanWrapperFieldSetMapper<ResultCSV> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ResultCSV.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

}
