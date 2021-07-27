package org.yangxin.rabbitmq.rabbitmqapi.api.dlx;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

        // 这就是一个普通的交换机和队列以及路由
        String exchangeName = "test_dlx_exchange";
        String queueName = "test_dlx_queue";
        String routingKey = "dlx.#";

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);

        Map<String, Object> argumentMap = new HashMap<>(1);
        argumentMap.put("x-dead-letter-exchange", "dlx.exchange");
        // 这个arguments属性，要设置到声明队列上
        channel.queueDeclare(queueName, true, false, false, argumentMap);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 要进行死信队列的声明
        channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
        channel.queueDeclare("dlx.queue", true, false, false, null);
        channel.queueBind("dlx.queue", "dlx.exchange", "#");

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
