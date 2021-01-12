package org.yangxin.rabbitmq.rabbitmqapi.api.limit;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/31/20 9:12 PM
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_qos_exchange";
        String queueName = "test_qos_queue";
        String routingKey = "qos.#";

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 限流方式第一件事就是autoAck设置为false
        channel.basicQos(0, 1, false);
        channel.basicConsume(queueName, false, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                log.info("消费者： [{}]", msg);

                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
