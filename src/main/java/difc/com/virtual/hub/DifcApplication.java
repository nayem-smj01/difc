package difc.com.virtual.hub;

import com.google.ortools.Loader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan

public class DifcApplication {

    public static void main(String[] args) {
        SpringApplication.run(DifcApplication.class, args);
    }
    static{
        Loader.loadNativeLibraries();
    }


}
