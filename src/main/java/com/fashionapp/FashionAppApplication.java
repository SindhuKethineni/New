package com.fashionapp;

import javax.annotation.Resource;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fashionapp.filestorage.FileStorage;

@SpringBootApplication
@Controller
@ComponentScan(basePackages = "com.*")
public class FashionAppApplication implements CommandLineRunner {
	private static final Logger log = LogManager.getLogger(FashionAppApplication.class);

	//@Autowired
	@Resource
	FileStorage filestorage;

	public static void main(String[] args) {
		SpringApplication.run(FashionAppApplication.class, args);
	}

	@RequestMapping(value = "/")
	@ResponseBody
	public String OnStartUp() {
		 
			log.debug("Debugging log");
			log.info("Info log");
			log.warn("Hey, This is a warning!");
			log.error("Oops! We have an Error. OK");
	  
		
		return "FashionApp is listening !...";
	}

	@Override
	public void run(String... args) throws Exception {
		//filestorage.deleteAll();
		filestorage.init();
	}
	//@Override
 	public void run(ApplicationArguments applicationArguments) throws Exception {
 		log.debug("Debugging log");
 		log.info("Info log");
 		log.warn("Hey, This is a warning!");
 		log.error("Oops! We have an Error. OK");
 		log.fatal("Damn! Fatal error. Please fix me.");
	}
	
}
