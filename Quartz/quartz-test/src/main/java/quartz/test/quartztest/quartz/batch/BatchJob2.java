package quartz.test.quartztest.quartz.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchJob2 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = "createJob2")
    public Job createJob2(){
        return jobBuilderFactory.get("createJob2")
                .start(createJob2_Step1())
                .build();
    }

    public Step createJob2_Step1(){
        return stepBuilderFactory.get("createJob2_Step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("createJob2_Step1 start!!");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
