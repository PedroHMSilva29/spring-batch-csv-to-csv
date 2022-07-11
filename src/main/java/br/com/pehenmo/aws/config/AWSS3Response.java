package br.com.pehenmo.aws.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSS3Response {

    private static final String BUCKET_NAME = "csv-test-s3-bucket";
    private static final String KEY_NAME = "responseStudent.csv";

    public String getBucketName() {
        return BUCKET_NAME;
    }

    public String getKeyName() {
        return KEY_NAME;
    }
}
