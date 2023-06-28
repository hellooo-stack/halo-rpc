package site.hellooo.rpc;

public class ClientConfig {
    public static final int DEFAULT_CONNECT_RETRY_TIMES = 10;
    public static final int DEFAULT_SEND_TIMEOUT_SECONDS = 5;
    private int connectRetryTimes = DEFAULT_CONNECT_RETRY_TIMES;
    private int sendTimeoutSeconds = DEFAULT_SEND_TIMEOUT_SECONDS;
    public ClientConfig() {

    }

    public int getConnectRetryTimes() {
        return connectRetryTimes;
    }

    public void setConnectRetryTimes(int connectRetryTimes) {
        this.connectRetryTimes = connectRetryTimes;
    }

    public int getSendTimeoutSeconds() {
        return sendTimeoutSeconds;
    }

    public void setSendTimeoutSeconds(int sendTimeoutSeconds) {
        this.sendTimeoutSeconds = sendTimeoutSeconds;
    }
}
