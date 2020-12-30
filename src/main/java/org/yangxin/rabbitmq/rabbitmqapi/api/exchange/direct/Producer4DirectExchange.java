package org.yangxin.rabbitmq.rabbitmqapi.api.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/30/20 5:17 PM
 */
public class Producer4DirectExchange {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        // 创建Channel
        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_direct_exchange";
        String routingKey = "test.direct";

        // 发送
        String msg = "Hello RabbitMQ for Direct Exchange Message.";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        channel.close();
        connection.close();
    }
}
