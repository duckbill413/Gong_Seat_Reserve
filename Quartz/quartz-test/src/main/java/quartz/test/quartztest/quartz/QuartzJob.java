package quartz.test.quartztest.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quartz.test.quartztest.job.Market;
import quartz.test.quartztest.job.MarketRepository;

@Slf4j
@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QuartzJob implements Job {
    @Autowired
    private MarketRepository marketRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Quartz Job Executed");

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        log.info("dataMap date : {}", dataMap.get("date"));
        log.info("dataMap executeCount : {}", dataMap.get("executeCount"));

        int cnt = (int) dataMap.get("executeCount");
        dataMap.put("executeCount", ++cnt);

        Market market = new Market();
        market.setName(String.format("duckbill_%s", dataMap.get("date")));
        market.setPrice(3000);
        marketRepository.save(market);
    }
}
