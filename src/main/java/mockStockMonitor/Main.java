package mockStockMonitor;

import mockStockMonitor.pipline.monitStock.PipelineDriver;

import java.lang.reflect.Field;

/**
 * main 方法类
 */
public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        GlobalConfig config = new GlobalConfig();

        for (String configItem : args) {
            int index = configItem.indexOf("=");

            if (index < 0) {
                continue;
            }

            String key = configItem.substring(0, index);
            String value = configItem.substring(index + 1, configItem.length());
            Field field = config.getClass().getDeclaredField(key);
            field.setAccessible(true);
            field.set(config, value);
        }

        new PipelineDriver(config).start();
    }
}