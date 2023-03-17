package br.com.pehenmo.batch.writer;

import br.com.pehenmo.batch.entity.Student;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class FileRequestWriter {

    public FlatFileItemWriter<Student> write(){
        return new FlatFileItemWriterBuilder<Student>()
                .name("csv-writer")
                .append(false)
                .resource(new FileSystemResource("src\\main\\resources\\csv\\output.csv"))
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
}
