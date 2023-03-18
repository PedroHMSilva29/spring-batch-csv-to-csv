package br.com.pehenmo.batch.writer;

import br.com.pehenmo.batch.ResultCSV;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class FileRequestWriter {

    private Resource outputResource = new FileSystemResource("src\\main\\resources\\csv\\output.csv");

    public FlatFileItemWriter<ResultCSV> write(){
        return new FlatFileItemWriterBuilder<ResultCSV>()
                .name("csv-writer")
                .append(true)
                .resource(outputResource)
                .lineAggregator(lineAggregator())
                .build();
    }

    private LineAggregator<ResultCSV> lineAggregator() {

        DelimitedLineAggregator<ResultCSV> lineAggregator = new DelimitedLineAggregator<>();

        BeanWrapperFieldExtractor<ResultCSV> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(ResultCSV.filds());

        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return lineAggregator;

    }
}
