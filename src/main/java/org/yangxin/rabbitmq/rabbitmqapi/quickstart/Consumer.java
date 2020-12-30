package org.yangxin.rabbitmq.rabbitmqapi.quickstart;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil.getConnection;

/**
 * @author yangxin
 * 12/30/20 4:08 PM
 */
@Slf4j
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
//        // 1. 创建一个ConnectionFactory，并进行配置
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("192.168.3.3");
//        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("/");
//        connectionFactory.setUsername("admin");
//        connectionFactory.setPassword("123456");
//
//        // 2. 通过连接工厂创建连接
//        Connection connection = connectionFactory.newConnection();
        Connection connection = getConnection();


        // 3. 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        // 4. 声明一个队列
        String queueName = "test001";
        channel.queueDeclare(queueName, true, false, false, null);

        // 5. 创建消费者
//        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel);
//        DefaultConsumer consumer = new DefaultConsumer(channel);

        // 6. 设置Channel
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) {
                String msg = new String(body);
                log.info("消费端：[{}]", msg);
            }
        });

        // 7. 获取消息
//        while (true) {
//            consumer.handleDelivery();
//        }
    }
}
