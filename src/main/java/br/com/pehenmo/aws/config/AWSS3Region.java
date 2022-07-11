package br.com.pehenmo.aws.config;

import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
public class AWSS3Region {

    private static final Region REGION = Region.US_EAST_2;

    public static Region getRegion() {
        return REGION;
    }
}
