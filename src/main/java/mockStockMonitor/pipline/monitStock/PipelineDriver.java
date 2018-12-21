package mockStockMonitor.pipline.monitStock;

import mockStockMonitor.GlobalConfig;
import mockStockMonitor.pipline.monitStock.model.Stocks;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PipelineDriver {
    private static final Logger logger = Logger.getLogger(PipelineDriver.class.getName());

    /**
     * 全局配置
     */
    private GlobalConfig globalConfig;

    public PipelineDriver(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    /**
     * 上次刷新后的股票持仓情况
     */
    private Stocks lastStocks;

    public void start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    RemoteWebDriver firefox = startDriver();
//                    firefox = RemoteWebDriverWrapper.enhance(firefox);
                    logger.log(Level.INFO, "启动 firefox");

                    while (true) {
                        try {
                            if (PipelineDriver.this.tradeTime()) {
                                start0(firefox);
                            }

                            logger.log(Level.INFO, "任务 wait 3 分钟。");
                            this.wait(3 * 60 * 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        thread.setName("主线程");
        thread.start();
    }

    /**
     * 开始工作
     */
    private void start0(RemoteWebDriver firefox) {
        // 红伊天使主页
        String redWare = "http://moni.10jqka.com.cn/228786";
        firefox.get(redWare);
        TryToLoginMockStock tryToLogin = new TryToLoginMockStock(firefox, globalConfig.getThsUsername(), globalConfig.getThsPassword());
        tryToLogin.tryToLogin();
        logger.log(Level.INFO, "尝试登陆成功");
        firefox.get(redWare);
        logger.log(Level.INFO, "跳转到 read wear 页面");
        CollectNewStock collectNewStock = new CollectNewStock(firefox);
        Stocks newStocks = collectNewStock.getNowStocks();
        logger.log(Level.INFO, "获取到新的 stock itme：" + (newStocks != null && newStocks.getStocks() != null ? newStocks.getStocks().size() : 0 ) + "条");
        CalculateDifference calculate = new CalculateDifference();
        String content = calculate.calculateDiffer(lastStocks, newStocks);
        logger.log(Level.INFO, "对比后有内容吗：" + (content != null && content.length() > 0));
        lastStocks = newStocks;

        if (content != null && content.length() > 0) {
            logger.log(Level.INFO, "尝试发送邮件通知");
            MailNotify notify = new MailNotify(globalConfig.getOutlookName(), globalConfig.getOutlookPass());
            notify.sendMail(globalConfig.getMailRecv(), "红衣天使-操作变动通知", content);
            logger.log(Level.INFO, "邮件通知发送完毕");
            logger.log(Level.INFO, "尝试发送 SMS 通知");
            SMSNotifyNode smsNotifyNode = new SMSNotifyNode(globalConfig.getAccessKey(), globalConfig.getSecret(), globalConfig.getSignature(), globalConfig.getTemplet());
            smsNotifyNode.send(globalConfig.getSmsRecv(), "红衣天使", "");
            logger.log(Level.INFO, "SMS 通知发送完毕:" + globalConfig.getSmsRecv());
        }
    }

    private RemoteWebDriver startDriver() {
        System.setProperty("webdriver.gecko.driver", globalConfig.getFirefoxDriverPath());
        System.setProperty("webdriver.firefox.bin", globalConfig.getFirefoxPath());
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        return new FirefoxDriver(options);
    }

    /**
     * 判断当前时间是否是交易时间
     */
    private boolean tradeTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 30);
        long startTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        long endTIme = calendar.getTimeInMillis();
        long now = System.currentTimeMillis();
        return now >= startTime && now <= endTIme;
    }
}
