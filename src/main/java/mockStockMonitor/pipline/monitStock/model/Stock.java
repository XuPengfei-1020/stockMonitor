package mockStockMonitor.pipline.monitStock.model;

/**
 * 股票仓位情况
 */
public class Stock {
    /**
     * 股票代码
     */
    private String id;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 股票数量
     */
    private int count;

    /**
     * 建仓时间
     */
    private String createTime;

    /**
     * 买入价格
     */
    private float buyPrice;

    /**
     * 现在价格
     */
    private float nowPrice;

    /**
     * 今日涨跌幅
     */
    private float todayFloat;

    /**
     * 股票总价值总额
     */
    private float assetValue;

    /**
     * 总盈利率
     */
    private float rates;

    /**
     * 总利润
     */
    private float interest;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public float getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(float buyPrice) {
        this.buyPrice = buyPrice;
    }

    public float getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(float nowPrice) {
        this.nowPrice = nowPrice;
    }

    public float getTodayFloat() {
        return todayFloat;
    }

    public void setTodayFloat(float todayFloat) {
        this.todayFloat = todayFloat;
    }

    public float getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(float assetValue) {
        this.assetValue = assetValue;
    }

    public float getRates() {
        return rates;
    }

    public void setRates(float rates) {
        this.rates = rates;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Stock)) {
            return false;
        }

        Stock stock = (Stock) obj;
        return this.id == null ? this == stock : this.id.equals(stock.getId());
    }
}