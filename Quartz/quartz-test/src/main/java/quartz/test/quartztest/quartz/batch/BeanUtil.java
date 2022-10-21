package quartz.test.quartztest.quartz.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Context 에서 해당하는 Bean을 찾아주는 역할
 */
@Component
@RequiredArgsConstructor
public class BeanUtil {
    private final ApplicationContext applicationContext;

    public Object getBean(String name){
        return applicationContext.getBean(name);
    }
}
