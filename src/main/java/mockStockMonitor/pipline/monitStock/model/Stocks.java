package mockStockMonitor.pipline.monitStock.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有的持仓。方便计算每个股票占总仓的量
 */
public class Stocks {
    /**
     * 总金额
     */
    private float totalMoney;

    /**
     * 当前的持仓股票
     */
    private Map<String, Stock> stocks = new HashMap<>();

    /**
     * 可用余额
     */
    private float balance;

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setStocks(Map<String, Stock> stocks) {
        this.stocks = stocks;
    }

    /**
     * copy 一份 return 出去，避免被外部更改
     * @return
     */
    public Map<String, Stock> getStocks() {
        HashMap<String, Stock> newMap = new HashMap<>(stocks.size());

        for (String id : stocks.keySet()) {
            newMap.put(id, stocks.get(id));
        }

        return newMap;
    }
}