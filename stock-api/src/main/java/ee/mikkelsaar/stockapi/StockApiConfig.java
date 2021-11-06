package ee.mikkelsaar.stockapi;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties(ConfigProperties.class)
@EnableScheduling
@Configuration
public class StockApiConfig {

}
