package org.yangxin.rabbitmq.rabbitmqapi.api.ack;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/31/20 9:12 PM
 */
@Slf4j
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_ack_exchange";
        String queueName = "test_ack_queue";
        String routingKey = "ack.#";

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        channel.basicConsume(queueName, false, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                log.info("消费者： [{}]", msg);

                Map<String, Object> headerMap = properties.getHeaders();
                if (!CollectionUtils.isEmpty(headerMap) && (int) headerMap.get("num") == 0) {
                    channel.basicNack(envelope.getDeliveryTag(), false, false);
                } else {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        });
    }
}
