package org.yangxin.rabbitmq.rabbitmqapi.api.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/30/20 7:14 PM
 */
public class Producer4FanoutExchange {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_fanout_exchange";

        // 发送
        for (int i = 0; i < 10; i++) {
            String msg = "Hello World RabbitMQ for Fanout Exchange Message.";
            channel.basicPublish(exchangeName, "", null, msg.getBytes());
        }

        channel.close();
        connection.close();
    }
}
