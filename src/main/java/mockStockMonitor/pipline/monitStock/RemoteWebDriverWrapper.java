package mockStockMonitor.pipline.monitStock;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 包装 RemoteWebDriver， 解决 remoteWebDriver 找不到 element 对象就抛出异常的毛病
 */
public class RemoteWebDriverWrapper {
    /**
     * 增强 RemoteWebDriver
     * @param remoteWebDriver
     * @return
     */
    public static RemoteWebDriver enhance(RemoteWebDriver remoteWebDriver) {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(remoteWebDriver.getClass().getClassLoader());
        enhancer.setSuperclass(remoteWebDriver.getClass());
        enhancer.setCallback(new FindMethodInterceptor(remoteWebDriver));
        return (RemoteWebDriver) enhancer.create();
    }

    /**
     * 方法拦截器实现类
     */
    private static class FindMethodInterceptor implements MethodInterceptor {
        private Object oriObj;

        public FindMethodInterceptor(Object oriObj) {
            this.oriObj = oriObj;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            if (method.getName().startsWith("findElementBy")) {
                try {
                    method.setAccessible(true);
                    Object rs = method.invoke(oriObj, args);
                    // 对于返回的单个的元素再做一次增强
                    return rs.getClass() == RemoteWebElement.class ? wrapperRemoteWebElement((RemoteWebElement) rs) : rs;
                } catch (NoSuchElementException ex) {
                    return null;
                }
            }

            if (method.getName().startsWith("findElementsBy")) {
                try {
                    method.setAccessible(true);
                    List<WebElement> rs = (List) method.invoke(oriObj, args);

                    // 对于返回 list 集合的多个元素结果中的每一个元素都做增强。
                    if (rs.size() > 0 && rs.get(0) instanceof RemoteWebElement) {
                        List<RemoteWebElement> enhanceElements = rs.getClass().getConstructor(Integer.class).newInstance(rs.size());

                        for (WebElement element : rs) {
                            enhanceElements.add(wrapperRemoteWebElement((RemoteWebElement) element));
                        }

                        return enhanceElements;
                    }
                } catch (NoSuchElementException ex) {
                    return null;
                }
            }

            method.setAccessible(true);
            return method.invoke(oriObj, args);
        }

        /**
         * 包装一个增强过的 remoteWebElement
         * @param webElement
         * @return
         */
        private RemoteWebElement wrapperRemoteWebElement(RemoteWebElement webElement) {
            Enhancer enhancer = new Enhancer();
            enhancer.setClassLoader(webElement.getClass().getClassLoader());
            enhancer.setSuperclass(webElement.getClass());
            enhancer.setCallback(new FindMethodInterceptor(webElement));
            return (RemoteWebElement) enhancer.create();
        }
    }
}