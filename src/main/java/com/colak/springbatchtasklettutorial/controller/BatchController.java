package com.colak.springbatchtasklettutorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BatchController {

    private final JobLauncher jobLauncher;

    private final Job job;

    // http://localhost:8080/job/test1
    @GetMapping("/job/{param}")
    public void startJob1(@PathVariable("param") String param) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("PARAM", param)
                .toJobParameters();
        jobLauncher.run(job, params);
    }
}
