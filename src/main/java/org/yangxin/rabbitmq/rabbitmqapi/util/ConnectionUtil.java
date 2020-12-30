package org.yangxin.rabbitmq.rabbitmqapi.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/30/20 4:58 PM
 */
public class ConnectionUtil {

    public static Connection getConnection() throws IOException, TimeoutException {
        // 创建一个ConnectionFactory，并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.3.3");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");

        // 通过连接工厂创建连接
        return connectionFactory.newConnection();
    }
}
