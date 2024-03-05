package org.edupoll.app;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppEtaApplication {

	public static void main(String[] args) {

//		File baseDir = new File("c:\\eta-upload");
//
//		File[] subDir = baseDir.listFiles();
//		for (File eachDir : subDir) {
//			File[] files = eachDir.listFiles();
//			for (File file : files) {
//				file.delete();
//			}
//			eachDir.delete();
//		}

		SpringApplication.run(AppEtaApplication.class, args);

	}

}
