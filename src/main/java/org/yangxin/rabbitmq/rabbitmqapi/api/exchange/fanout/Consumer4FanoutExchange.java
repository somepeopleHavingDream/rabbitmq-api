package org.yangxin.rabbitmq.rabbitmqapi.api.exchange.fanout;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/30/20 7:16 PM
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class Consumer4FanoutExchange {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_fanout_exchange";
        String exchangeType = "fanout";
        String queueName = "test_fanout_queue";
        // 不设置路由键
        String routingKey = "";
        channel.exchangeDeclare(exchangeName,
                exchangeType,
                true,
                false,
                false,
                null);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // durable：是否持久化消息
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
