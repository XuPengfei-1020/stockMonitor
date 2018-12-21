package mockStockMonitor.pipline.monitStock;

import mockStockMonitor.pipline.monitStock.model.Stock;
import mockStockMonitor.pipline.monitStock.model.Stocks;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.*;

/**
 * 在跳转到交易者页面后，负责收集新的持仓数据
 */
public class CollectNewStock {
    /**
     * 持有的 web driver
     */
    private RemoteWebDriver driver;

    /**
     * Constructor
     * @param driver
     */
    public CollectNewStock(RemoteWebDriver driver) {
        this.driver = driver;
    }

    /**
     * 获取新的交易者的持仓情况
     * @return
     */
    public Stocks getNowStocks() {
        if (isEmptyStock(driver)) {
            return new Stocks();
        }

        String tableId = "ccqk_tbl";
        RemoteWebElement element = (RemoteWebElement) driver.findElementById(tableId);
        RemoteWebElement tbodyElement = (RemoteWebElement) element.findElementByTagName("tbody");
        List<WebElement> trElements = tbodyElement.findElementsByTagName("tr");
        Map<String, Stock> newStocks = new HashMap<>(trElements.size());

        for (WebElement tre : trElements) {
            RemoteWebElement tr = (RemoteWebElement) tre;
            List<WebElement> tds = tr.findElementsByTagName("td");
            Stock stock = buildStockMedel(tds);
            newStocks.put(stock.getId(), stock);
        }

        Stocks stocks = collectTotalMoney(driver);
        stocks.setStocks(newStocks);
        return stocks;
    }

    /**
     * 判断目前股票是否是空仓
     * @param driver
     * @return true 表示空仓， false 表示不是空仓
     */
    private boolean isEmptyStock(RemoteWebDriver driver) {
        List<WebElement> elements = driver.findElementsByClassName("p-no2");

        for (WebElement webElement : elements) {
            // 判断股票是否是空仓
            if("该用户目前空仓！".equals(webElement.getText())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 从一组 WebElement （多个 td 标签）中构建 stock 对象
     * @param tds 多个 td 标签，每个标签表示一个 stock 参数
     * @return 构建后的 stock 对象
     */
    private Stock buildStockMedel(List<WebElement> tds) {
        Stock stock = new Stock();
        stock.setId(tds.get(0).getText());
        stock.setName(tds.get(1).getText());
        stock.setCount(Integer.valueOf(tds.get(2).getText()));
        stock.setCreateTime(tds.get(3).getText());
        stock.setBuyPrice(Float.valueOf(tds.get(4).getText()));
        stock.setNowPrice(Float.valueOf(tds.get(5).getText()));
        String todayFloatStr = tds.get(6).getText();
        // 去掉末尾的 %
        todayFloatStr = todayFloatStr.endsWith("%") ?
            todayFloatStr.substring(0, todayFloatStr.length() - 1) : todayFloatStr;
//        stock.setTodayFloat(Float.valueOf(todayFloatStr));
        stock.setAssetValue(Float.valueOf(tds.get(7).getText()));
        String rateStr = tds.get(8).getText();
        rateStr = rateStr.endsWith("%") ?
            rateStr.substring(0, rateStr.length() - 1) : rateStr;
        stock.setRates(Float.valueOf(rateStr));
        stock.setInterest(Float.valueOf(tds.get(9).getText()));
        return stock;
    }

    /**
     * 获取此账户目前总的资金金额，用于计算当前持股股票比例
     * @param driver
     * @return
     */
    private Stocks collectTotalMoney(RemoteWebDriver driver) {
        RemoteWebElement div = (RemoteWebElement) driver.findElementById("myTab_Content0");
        List<RemoteWebElement> children = (List) div.findElementsByTagName("tr");
        div = children.get(children.size() - 1);
        children = (List) div.findElementsByTagName("td");
        Stocks stocks = new Stocks();
        stocks.setTotalMoney(Float.valueOf(children.get(0).getText()));
        stocks.setBalance(Float.valueOf(children.get(2).getText()));
        return stocks;
    }
}