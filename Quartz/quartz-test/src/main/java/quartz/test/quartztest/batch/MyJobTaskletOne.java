package quartz.test.quartztest.batch;

import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MyJobTaskletOne {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = "myJobTaskletOne_Job1")
    public Job myJobTaskletOne_Job1(){
        return this.jobBuilderFactory.get("myJobTaskletOne_Job1")
                .start(myJobTaskletOne_Job1_Step1())
                .next(myJobTaskletOne_Job1_Step2())
                .build();
    }

    @Bean
    public Step myJobTaskletOne_Job1_Step1(){
        return stepBuilderFactory.get("myJobTaskletOne_Job1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("myJobTaskletOne_Job1_Step1");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step myJobTaskletOne_Job1_Step2(){
        return stepBuilderFactory.get("myJobTaskletOne_Job1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("myJobTaskletOne_Job1_Step2");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
