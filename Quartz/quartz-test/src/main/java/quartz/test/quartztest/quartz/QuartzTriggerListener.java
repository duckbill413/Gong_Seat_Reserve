package quartz.test.quartztest.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

@Slf4j
public class QuartzTriggerListener implements TriggerListener {
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.info("Trigger 실행");
    }

    /**
     * @Content : 결과가 true 이면 JobListener jobExecutionVetoed(Job중단) 실행
     * @param trigger
     *          The <code>Trigger</code> that has fired.
     * @param context
     *          The <code>JobExecutionContext</code> that will be passed to
     *          the <code>Job</code>'s<code>execute(xx)</code> method.
     * @return
     */
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        log.info("Trigger 상태 체크");
        JobDataMap map = context.getJobDetail().getJobDataMap();

        int executeCount = 1;
        if (map.containsKey("executeCount")){
            executeCount = (int) map.get("executeCount");
        }
        return executeCount >= 2;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        log.info("Trigger 성공");
    }
}
