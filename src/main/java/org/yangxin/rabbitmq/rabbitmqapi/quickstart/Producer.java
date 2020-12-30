package org.yangxin.rabbitmq.rabbitmqapi.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.yangxin.rabbitmq.rabbitmqapi.utils.ConnectionUtil.getConnection;

/**
 * @author yangxin
 * 12/30/20 3:20 PM
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = getConnection();

        // 3. 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        // 4. 通过Channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "Hello RabbitMQ!";
            channel.basicPublish("", "test001", null, msg.getBytes());
        }

        // 5. 记得要关闭相关的连接
        channel.close();
        connection.close();
    }

//    private static Connection getConnection() throws IOException, TimeoutException {
//        // 1. 创建一个ConnectionFactory，并进行配置
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("192.168.3.3");
//        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("/");
//        connectionFactory.setUsername("admin");
//        connectionFactory.setPassword("123456");
//
//        // 2. 通过连接工厂创建连接
//        return connectionFactory.newConnection();
//    }
}
