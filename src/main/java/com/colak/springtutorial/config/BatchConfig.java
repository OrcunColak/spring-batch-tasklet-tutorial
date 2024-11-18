package com.colak.springtutorial.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final DataSource dataSource;

    // Create a job with two steps
    // Each step is a tasklet
    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("job1", jobRepository)
                .preventRestart()
                .start(step1(jobRepository, transactionManager))
                .next(removeDuplicateDataStep(jobRepository,transactionManager))
                .next(exceptionStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step_one", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                        log.info("STEP1 EXECUTED");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    Step exceptionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step_two", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("STEP2 EXECUTED");
                        boolean isTrue = true;
                        if (isTrue) {
                            throw new Exception("Exception occurred!!");
                        }
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    Step removeDuplicateDataStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("removeDuplicateDataStep", jobRepository)
                .tasklet(new RemoveDuplicateData(dataSource), platformTransactionManager)
                .build();
    }
}
