package org.yangxin.rabbitmq.rabbitmqapi.api.ack;

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

        String exchangeName = "test_ack_exchange";
        String routingKey = "ack.save";

        String msg = "Hello RabbitMQ ACK Message!";
        for (int i = 0; i < 5; i++) {
            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("num", i);
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .headers(headerMap)
                    .build();
            channel.basicPublish(exchangeName, routingKey, false, properties, msg.getBytes());
//            channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
        }
    }
}
