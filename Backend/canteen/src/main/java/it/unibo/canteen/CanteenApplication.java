package it.unibo.canteen;

import java.io.IOException;
import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
public class CanteenApplication implements ApplicationListener<ApplicationReadyEvent>{
	public static String myipAddr = "";
	public static String myport   = "0";
	
	public static void main(String[] args) throws IOException {
	    SpringApplication.run(CanteenApplication.class, args);
	}
	
    @Autowired
    private ApplicationContext applicationContext;
    
    @Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
	    try {
	        String ip = InetAddress.getLocalHost().getHostAddress();
	        int port = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 8080);
	        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" );
	        System.out.printf("IP=%s:PORT=%d", ip,port );
	        myipAddr = ip;
	        myport   = ""+port; 
	        System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" );
	    } catch ( Exception e) {
	    	e.printStackTrace();
	    }
	}
    
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
      return registry -> registry.config().commonTags("application", "CanteenApplication");
    }

}
