package mockStockMonitor.pipline.monitStock;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * 尝试登陆模拟炒股账户
 *
 * @author flying
 */
public class TryToLoginMockStock {
    /**
     * 当前浏览器对象
     */
    private RemoteWebDriver driver;

    /**
     * 当前尝试次数
     */
    private int tryTimes = 1;

    /**
     * 最多尝试次数
     */
    private int maxTryTimes = 3;

    /**
     * 登陆用户名
     */
    private String userName;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * Constructor
     * @param driver 当前浏览器对象
     */
    public TryToLoginMockStock(RemoteWebDriver driver, String userName, String password) {
        this.driver = driver;
        this.userName = userName;
        this.password = password;
    }

    /**
     * 尝试登陆
     */
    public void tryToLogin() {
        if(needLogin(driver)) {
            if (++tryTimes > 3) {
                throw new RuntimeException("尝试登陆了 " + maxTryTimes + " 失败，终止尝试");
            }

            // 尝试登陆
            doLogin(driver);
            this.tryToLogin();
        }
    }

    /**
     * 登陆动作，找到用户名和密码输入框，然后登陆
     * @param driver 浏览器
     */
    private void doLogin(RemoteWebDriver driver) {
        final String loginUrl = "http://upass.10jqka.com.cn/doLogin?redir=HTTP_REFERER";
        final String userNameInput = "username";
        final String passwordInput = "password";
        final String loginBtn = "loginBtn";
        driver.get(loginUrl);
        WebElement userNameInputElement = driver.findElementById(userNameInput);
        userNameInputElement.click();
        userNameInputElement.sendKeys(userName);
        WebElement passwordElement = driver.findElementById(passwordInput);
        passwordElement.click();
        passwordElement.sendKeys(password);
        WebElement submitBtn = driver.findElementById(loginBtn);
        submitBtn.click();
    }

    /**
     * 通过判断是否有登陆标志来判定当前是否登陆成功
     * @param driver 浏览器
     */
    private boolean needLogin(RemoteWebDriver driver) {
        final String loginFlag = "header_login";

        try {
            WebElement webElement = driver.findElementById(loginFlag);
            return webElement.getText().contains("登 录");
        } catch (Exception e) {
        }

        return true;
    }
}