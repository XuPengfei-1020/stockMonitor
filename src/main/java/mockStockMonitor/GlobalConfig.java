package mockStockMonitor;

public class GlobalConfig {
    private String firefoxDriverPath;
    private String firefoxPath;
    private String mailRecv;
    private String smsRecv;
    private String outlookName;
    private String outlookPass;

    private String thsUsername;
    private String thsPassword;

    // sms 配置
    private String accessKey;
    private String secret;
    private String signature;
    private String templet;

    public String getFirefoxDriverPath() {
        return firefoxDriverPath;
    }

    public void setFirefoxDriverPath(String firefoxDriverPath) {
        this.firefoxDriverPath = firefoxDriverPath;
    }

    public String getFirefoxPath() {
        return firefoxPath;
    }

    public void setFirefoxPath(String firefoxPath) {
        this.firefoxPath = firefoxPath;
    }

    public String getMailRecv() {
        return mailRecv;
    }

    public void setMailRecv(String mailRecv) {
        this.mailRecv = mailRecv;
    }

    public String getSmsRecv() {
        return smsRecv;
    }

    public void setSmsRecv(String smsRecv) {
        this.smsRecv = smsRecv;
    }

    public String getOutlookName() {
        return outlookName;
    }

    public void setOutlookName(String outlookName) {
        this.outlookName = outlookName;
    }

    public String getOutlookPass() {
        return outlookPass;
    }

    public void setOutlookPass(String outlookPass) {
        this.outlookPass = outlookPass;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTemplet() {
        return templet;
    }

    public void setTemplet(String templet) {
        this.templet = templet;
    }

    public String getThsUsername() {
        return thsUsername;
    }

    public void setThsUsername(String thsUsername) {
        this.thsUsername = thsUsername;
    }

    public String getThsPassword() {
        return thsPassword;
    }

    public void setThsPassword(String thsPassword) {
        this.thsPassword = thsPassword;
    }
}