package br.com.pehenmo.batch.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "batch")
public class BatchProperties {

    private String inputCsvFileSource = "src\\main\\resources\\csv\\input.csv";
    private String outputCsvFileSource = "src\\main\\resources\\csv\\output.csv";

    public String getInputCsvFileSource() {
        return inputCsvFileSource;
    }

    public void setInputCsvFileSource(String inputCsvFileSource) {
        this.inputCsvFileSource = inputCsvFileSource;
    }

    public String getOutputCsvFileSource() {
        return outputCsvFileSource;
    }

    public void setOutputCsvFileSource(String outputCsvFileSource) {
        this.outputCsvFileSource = outputCsvFileSource;
    }

}
