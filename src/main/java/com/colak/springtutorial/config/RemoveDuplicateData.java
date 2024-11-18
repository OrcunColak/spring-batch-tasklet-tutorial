package com.colak.springtutorial.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

// This class is an example for a Tasklet that executes a JDBC task
@Slf4j
public class RemoveDuplicateData implements Tasklet {

    protected final DataSource dataSource;

    protected final JdbcTemplate jdbcTemplate;

    public RemoveDuplicateData(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        String sql = "DELETE FROM numbers t1 WHERE t1.id > (SELECT MIN(t2.id) FROM numbers t2 WHERE t1.number = t2.number)";

        jdbcTemplate.execute(sql);

        log.info("RemoveDuplicateData EXECUTED");

        return RepeatStatus.FINISHED;
    }
}
