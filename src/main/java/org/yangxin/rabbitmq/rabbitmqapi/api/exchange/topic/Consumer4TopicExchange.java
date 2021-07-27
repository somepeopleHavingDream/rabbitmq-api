package org.yangxin.rabbitmq.rabbitmqapi.api.exchange.topic;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/30/20 6:57 PM
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class Consumer4TopicExchange {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_topic_exchange";
        String exchangeType = "topic";
        String queueName = "test_topic_queue";
        String routingKey = "user.*";

        // 声明交换机
        channel.exchangeDeclare(exchangeName,
                exchangeType,
                true,
                false,
                false,
                null);
        // 声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 建立交换机和队列的绑定关系
        channel.queueBind(queueName, exchangeName, routingKey);

        // durable：是否持久化消息
        // 参数：队列名称、是否自动ack、Consumer
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) {
                String msg = new String(body);
                log.info("收到消息：[{}]", msg);
            }
        });
    }
}
