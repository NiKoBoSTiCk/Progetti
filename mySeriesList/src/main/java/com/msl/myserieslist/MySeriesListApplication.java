package com.msl.myserieslist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.msl.myserieslist.repositories")
public class MySeriesListApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySeriesListApplication.class, args);
    }

}
