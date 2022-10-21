package quartz.test.quartztest.quartz.batch;

import lombok.SneakyThrows;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quartz.test.quartztest.quartz.QuartzJob;

import java.util.Date;

@Component
public class QuartzBatchJob extends QuartzJob {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private BeanUtil beanUtil;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        // 전달받은 JobDataMap에서 Job 이름을 꺼내오고 그 Job 이름으로 context에서 bean을 가져온다.
        Job job = (Job) beanUtil.getBean((String) jobDataMap.get(QuartzBatchService.JOB_ANME));

        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("curDate", new Date())
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
