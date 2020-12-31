package org.yangxin.rabbitmq.rabbitmqapi.api.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/31/20 9:08 PM
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.save";

        String msg = "Hello RabbitMQ DLX Message!";
        for (int i = 0; i < 1; i++) {
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    // 2：代表持久化投递
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .expiration("10000")
                    .build();
            channel.basicPublish(exchangeName, routingKey, false, properties, msg.getBytes());
        }
    }
}
