package com.example.demo;

import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws InterruptedException, IOException {

		final Logger log = LoggerFactory.getLogger(DemoApplication.class);

		log.info("INICIADO REGISTRO DE MÉTRICAS DA JVM");
		JvmMetrics.builder().register(); // initialize the out-of-the-box JVM metrics

		Counter counter = Counter.builder()
				.name("my_count_total")
				.help("example counter")
				.labelNames("status")
				.register();

		counter.labelValues("ok").inc();
		counter.labelValues("ok").inc();
		counter.labelValues("error").inc();

		log.info("INICIADO SERVIDOR DE MÉTRICAS NA PORTA 9400");
		HTTPServer server = HTTPServer.builder()
				.port(9400)
				.buildAndStart();

		System.out.println("HTTPServer listening on port http://localhost:" + server.getPort() + "/metrics");
		SpringApplication.run(DemoApplication.class, args);
		Thread.currentThread().join(); // sleep forever
	}

}
