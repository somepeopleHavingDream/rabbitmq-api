package org.yangxin.rabbitmq.rabbitmqapi.api.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/31/20 9:08 PM
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_qos_exchange";
        String routingKey = "qos.save";

        String msg = "Hello RabbitMQ QOS Message!";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName, routingKey, false, null, msg.getBytes());
//            channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
        }
    }
}
