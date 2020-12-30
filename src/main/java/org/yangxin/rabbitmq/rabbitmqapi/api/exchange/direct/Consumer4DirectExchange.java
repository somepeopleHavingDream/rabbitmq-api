package org.yangxin.rabbitmq.rabbitmqapi.api.exchange.direct;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/30/20 5:22 PM
 */
@Slf4j
public class Consumer4DirectExchange {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_direct_exchange";
        String exchangeType = "direct";
        String queueName = "test_direct_queue";
        String routingKey = "test.direct";

        // 表示声明了一个交换机
//        channel.exchangeDeclare(exchangeName, exchangeType, true);
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        // 表示声明了一个队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 建立一个绑定关系
        channel.queueBind(queueName, exchangeName, routingKey);

        // durable：是否持久化消息
        // 参数：队列名称、是否自动ack、Consumer
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String msg = new String(body);
                log.info("收到消息：[{}]", msg);
            }
        });
    }
}
