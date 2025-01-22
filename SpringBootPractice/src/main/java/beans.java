import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

class MyBean implements InitializingBean, DisposableBean {

    public MyBean() {
        System.out.println("1. Bean is instantiated");
    }

    private String property;

    public void setProperty(String property) {
        this.property = property;
        System.out.println("2. Property is set: " + property);
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("3. @PostConstruct method called");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("4. InitializingBean's afterPropertiesSet method called");
    }

    public void customInit() {
        System.out.println("5. Custom init-method called");
    }

    // Bean is Ready
    public void doSomething() {
        System.out.println("6. Bean is ready to use: Property = " + property);
    }

    // Pre-Destruction
    @PreDestroy
    public void preDestroy() {
        System.out.println("7. @PreDestroy method called");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("8. DisposableBean's destroy method called");
    }
    public void customDestroy() {
        System.out.println("9. Custom destroy-method called");
    }
}

@Configuration
class AppConfig {
    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public MyBean myBean() {
        MyBean bean = new MyBean();
        bean.setProperty("Example Property");
        return bean;
    }
}

public class BeanLifecycleExample {
    public static void main(String[] args) {
        // Create application context

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get the bean and use it
        MyBean myBean = context.getBean(MyBean.class);
        myBean.doSomething();

        // Close context to trigger destruction
        context.close();
    }
}
