package quartz.test.quartztest.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import quartz.test.quartztest.job.Product;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MyJobTwo {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean(name = "myJobTwo_Job1")
    public Job myJobTwo_Job1(){
        return jobBuilderFactory.get("myJobTwo_Job1")
                .start(myJobTwo_Job1_Step1())
                .build();
    }

    @Bean
    @JobScope
    public Step myJobTwo_Job1_Step1(){
        return stepBuilderFactory.get("myJobTwo_Job1_Step1")
                .<Product, Product>chunk(10)
                .reader(reader(null))
                .processor(processor(null))
                .writer(writer(null))
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Product> reader(@Value("#{jobParameters[requestDate]}") String requestDate){
        log.info("[Product Reader Start!] : {}", requestDate);
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("price", 1000);
        return new JpaPagingItemReaderBuilder<Product>()
                .pageSize(10)
                .queryString("SELECT m FROM Product m WHERE m.price >= : price")
                .parameterValues(parameterValues)
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }

    @Bean
    @JobScope
    public ItemProcessor<Product, Product> processor(@Value("#{jobParameters[requestDate]}") String requestDate){
        return item -> {
            log.info("[Product Process Start!] : {}", item);
            item.setName(String.format("%s_%s_%s", "duckbill", requestDate, item.getName()));
            return item;
        };
    }

    @Bean
    @JobScope
    public JpaItemWriter<Product> writer(@Value("#{jobParameters[requestDate]}") String requestDate){
        log.info("[Product Writer Start!] : {}", requestDate);
        return new JpaItemWriterBuilder<Product>()
                .entityManagerFactory(entityManagerFactory).build();
    }
}
