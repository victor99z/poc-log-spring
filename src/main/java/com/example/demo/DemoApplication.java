package com.example.demo;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.logging.Log4j2Metrics;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.core.instrument.binder.tomcat.TomcatMetrics;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import org.apache.catalina.Context;
import org.apache.catalina.Manager;
import org.apache.catalina.manager.ManagerServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.catalina.Manager;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.core.StandardService;
import org.apache.catalina.startup.Tomcat;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collections;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws InterruptedException, IOException {

		final Logger log = LoggerFactory.getLogger(DemoApplication.class);

		log.info("INICIADO REGISTRO DE MÉTRICAS DA JVM");

		PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

		Manager manager = null;

		new ClassLoaderMetrics().bindTo(prometheusRegistry);
		new JvmMemoryMetrics().bindTo(prometheusRegistry);
		new JvmGcMetrics().bindTo(prometheusRegistry);
		new ProcessorMetrics().bindTo(prometheusRegistry);
		new JvmThreadMetrics().bindTo(prometheusRegistry);
		new ProcessorMetrics().bindTo(prometheusRegistry);
		new TomcatMetrics(manager, Collections.EMPTY_LIST).bindTo(prometheusRegistry);
		new DiskSpaceMetrics(new File(System.getProperty("user.dir"))).bindTo(prometheusRegistry);
		new UptimeMetrics().bindTo(prometheusRegistry);

		HTTPServer.builder()
				.port(9400)
				.registry(prometheusRegistry.getPrometheusRegistry())
				.buildAndStart();

		SpringApplication.run(DemoApplication.class, args);

//		JvmMetrics.builder().register(); // initialize the out-of-the-box JVM metrics
//
//		Counter counter = Counter.builder()
//				.name("my_count_total")
//				.help("example counter")
//				.labelNames("status")
//				.register();
//
//		counter.labelValues("ok").inc();
//		counter.labelValues("ok").inc();
//		counter.labelValues("error").inc();
//
//		log.info("INICIADO SERVIDOR DE MÉTRICAS NA PORTA 9400");
//		HTTPServer server = HTTPServer.builder()
//				.port(9400)
//				.buildAndStart();
//
//		System.out.println("HTTPServer listening on port http://localhost:" + server.getPort() + "/metrics");
//		SpringApplication.run(DemoApplication.class, args);
//		Thread.currentThread().join(); // sleep forever
	}

}
