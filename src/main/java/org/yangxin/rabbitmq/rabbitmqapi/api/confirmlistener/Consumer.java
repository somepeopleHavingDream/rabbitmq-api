package org.yangxin.rabbitmq.rabbitmqapi.api.confirmlistener;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/31/20 6:01 PM
 */
@Slf4j
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.#";
        String queueName = "test_confirm_queue";

        // 声明交换机和队列，然后进行绑定设置，最后指定路由Key（此处既可以在生产端声明，也可在消费端声明）
        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 创建消费者
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) {
                String msg = new String(body);
                log.info("消费端：" + msg);
            }
        });
    }
}
