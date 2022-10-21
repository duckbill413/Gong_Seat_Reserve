package quartz.test.quartztest.quartz.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = "createJob1")
    public Job createJob1(){
        return jobBuilderFactory.get("createJob1")
                .start(createJob1_Step1())
                .build();
    }

    public Step createJob1_Step1(){
        return stepBuilderFactory.get("createJob1_Step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("createJob1_Step1 start!!");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
