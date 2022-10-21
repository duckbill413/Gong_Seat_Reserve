package quartz.test.quartztest.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzService {
    private final Scheduler scheduler;

    @PostConstruct
    public void init(){
        try{
            // 스케줄러 초기화 -> DB도 clear
            scheduler.clear();

            // job listener 등록
            scheduler.getListenerManager().addJobListener(new QuartzJobListener());
            scheduler.getListenerManager().addTriggerListener(new QuartzTriggerListener());

            Map paramsMap = new HashMap<>();
            paramsMap.put("executeCount", 1);
            paramsMap.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss"
            )));

            // Job 생성 및 스케줄러 등록
            addJob(QuartzJob.class, "QuartzJob", "Quartz Job 입니다.", paramsMap, "0/5 * * * * ?");
        } catch (Exception e) {
            log.error("addJob Error : {}", e);
        }
    }

    public <T extends Job> void addJob(Class<? extends Job> job, String name, String dsec, Map paramsMap, String cron) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(job, name, dsec, paramsMap);
        Trigger trigger = buildCronTrigger(cron);
        if (scheduler.checkExists(jobDetail.getKey()))
            scheduler.deleteJob(jobDetail.getKey());
        scheduler.scheduleJob(jobDetail, trigger);
    }

    // Job Detail create
    public <T extends Job> JobDetail buildJobDetail(Class<? extends Job> job, String name, String desc, Map paramsMap) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(paramsMap);

        return JobBuilder
                .newJob(job)
                .withIdentity(name)
                .withDescription(desc)
                .usingJobData(jobDataMap)
                .build();
    }

    private Trigger buildCronTrigger(String cronExp){
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();
    }
}
