package quartz.test.quartztest.quartz.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;
import quartz.test.quartztest.quartz.QuartzJobListener;
import quartz.test.quartztest.quartz.QuartzTriggerListener;

import javax.annotation.PostConstruct;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzBatchService {
    private final Scheduler scheduler;
    public static final String JOB_ANME = "JOB_NAME";

    @PostConstruct
    public void init(){
        try{
            scheduler.clear();
            scheduler.getListenerManager().addJobListener(new QuartzJobListener());
            scheduler.getListenerManager().addTriggerListener(new QuartzTriggerListener());

            // addJob(QuartzJob.class, "QuartzJob", "Quartz Job 입니다", paramsMap, cron);
            addJob(QuartzBatchJob.class, "createJob1", "createJob1 입니다", null, "0/5 * * * * ?");
            addJob(QuartzBatchJob.class, "createJob2", "createJob2 입니다", null, "0/10 * * * * ?");
        } catch (Exception e){
            log.error("addJob error : {}", e);
        }
    }
    public <T extends Job> void addJob(Class<? extends Job> job, String name, String desc, Map paramsMap, String cron) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(job, name, desc, paramsMap);
        Trigger trigger = buildCronTrigger(cron);

        if (scheduler.checkExists(jobDetail.getKey()))
            scheduler.deleteJob(jobDetail.getKey());
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public <T extends Job> JobDetail buildJobDetail(Class<? extends Job> job, String name, String dsec, Map paramsMap) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JOB_ANME, name);
        jobDataMap.put("executeCount", 1);

        return JobBuilder
                .newJob(job)
                .withIdentity(name)
                .withDescription(dsec)
                .usingJobData(jobDataMap)
                .build();
    }
    public Trigger buildCronTrigger(String cronExp){
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();
    }
}
