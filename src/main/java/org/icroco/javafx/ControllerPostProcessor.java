package org.icroco.javafx;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

//@Component
@Slf4j
@AllArgsConstructor
public class ControllerPostProcessor implements BeanPostProcessor {

    private final ViewLoader                     viewLoader;
    private final ConfigurableApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object proxy = this.getTargetObject(bean);
//        if (bean.getClass().getName().startsWith("org.icroco")) {
//            log.info("Process {}, name: {}", bean.getClass().getName(), beanName);
//        }

        final FxViewBinding annotation = AnnotationUtils.getAnnotation(proxy.getClass(), FxViewBinding.class);
        if (annotation != null) {
            log.info("Found View: '{}', fxViewBinding: {}", proxy.getClass().getSimpleName(), annotation);
            try {
                var si = applicationContext.getBean(annotation.id(), SceneInfo.class);
                log.warn("Bean Already defined: {}", si);
            }
            catch (Exception ex) {
                var si = viewLoader.loadView(proxy);
//            log.info("REgister bean: ", beanName + "-info");
//            GenericBeanDefinition gbd = new GenericBeanDefinition();
//            gbd.setBeanClass(proxy.getClass());

                applicationContext.getBeanFactory().registerSingleton(annotation.id(), si);
                log.debug("Bean Created: {}", applicationContext.getBean(annotation.id(), SceneInfo.class));
            }

        }
        return bean;
    }

    private Object getTargetObject(Object proxy) throws BeansException {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            try {
                return ((Advised) proxy).getTargetSource().getTarget();
            }
            catch (Exception e) {
                throw new FatalBeanException("Error getting target of JDK proxy", e);
            }
        }
        return proxy;
    }
}
