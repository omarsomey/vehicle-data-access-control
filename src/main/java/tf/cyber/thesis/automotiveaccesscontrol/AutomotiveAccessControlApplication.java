package tf.cyber.thesis.automotiveaccesscontrol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories("tf.cyber.thesis.automotiveaccesscontrol")
public class AutomotiveAccessControlApplication implements CommandLineRunner {

	private static Logger LOG = LoggerFactory
			.getLogger(AutomotiveAccessControlApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AutomotiveAccessControlApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("EXECUTING : command line runner");


	}

}
