package org.yangxin.rabbitmq.rabbitmqapi.api.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil.getConnection;

/**
 * @author yangxin
 * 12/30/20 3:20 PM
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = getConnection();

        // 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        Map<String, Object> headerMap = new HashMap<>(2);
        headerMap.put("my1", "my1");
        headerMap.put("my2", "my2");

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                // 2：代表持久化投递
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000")
                .headers(headerMap)
                .build();

        // 通过Channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "Hello RabbitMQ!";
            channel.basicPublish("", "test001", properties, msg.getBytes());
        }

        // 记得要关闭相关的连接
        channel.close();
        connection.close();
    }
}
