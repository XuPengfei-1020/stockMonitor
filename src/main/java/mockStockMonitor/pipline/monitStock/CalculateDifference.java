package mockStockMonitor.pipline.monitStock;

import mockStockMonitor.pipline.monitStock.model.Stock;
import mockStockMonitor.pipline.monitStock.model.Stocks;

import java.util.Map;

/**
 * 比较新旧股票持仓的差异。
 */
public class CalculateDifference {
    /**
     * 对比新旧持仓有什么不同，如果有不同，则计算出变化
     * @param oldStocksOverview 旧持仓
     * @param newStocksOverview 新持仓
     * @return 如果返回空字符串或者 null，则说明没有 difference。否则返回 difference 的内容
     */
    public String calculateDiffer(Stocks oldStocksOverview, Stocks newStocksOverview) {
        if (oldStocksOverview == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        Map<String, Stock> oldStocks = oldStocksOverview.getStocks();
        Map<String, Stock> newStocks = newStocksOverview.getStocks();

        for (String  oldId : oldStocks.keySet()) {
            Stock oldStock = oldStocks.get(oldId);
            Stock newStock = newStocks.remove(oldId);

            if (newStock == null) {
                result.append("[" + oldStock.getName() + "] 已经被清仓\n\n");
                continue;
            }

            int changedNum = newStock.getCount() - oldStock.getCount();

            if (changedNum != 0) {
                result.append("[" + oldStock.getName() + "] 仓位变化：[" + (changedNum > 0 ? "+" : "-") + ((Math.abs(changedNum)) / 100) + "手], ");
                result.append((changedNum > 0 ? "增持" : "减持[") + ((float)(Math.abs(changedNum)) / oldStock.getCount() * 100) + "%],");
                result.append("占比：[" + (newStock.getAssetValue()) / newStocksOverview.getTotalMoney() * 100 + "%]");
                result.append('\n');
            }
        }

        for (Stock  newCreate : newStocks.values()) {
            result.append("新建仓：[" + newCreate.getName() + "]，买入[" + (newCreate.getCount() / 100) + "] 手，");
            result.append("占比：[" + (newCreate.getAssetValue()) / newStocksOverview.getTotalMoney() * 100 + "%]");
            result.append("\n");
        }

        return result.toString();
    }
}