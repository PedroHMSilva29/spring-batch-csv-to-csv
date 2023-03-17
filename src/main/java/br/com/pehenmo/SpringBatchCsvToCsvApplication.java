package br.com.pehenmo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@ConfigurationPropertiesScan("br.com.pehenmo.batch.util")
@EnableScheduling
public class SpringBatchCsvToCsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchCsvToCsvApplication.class, args);
		//https://docs.spring.io/spring-batch/docs/current/reference/html/processor.html#filteringRecords
	}



}
