package org.yangxin.rabbitmq.rabbitmqapi.api.confirmlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/31/20 5:49 PM
 */
@Slf4j
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 指定我们的消息投递模式：消息的确认模式
        channel.confirmSelect();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";

        // 最好是先监听后发送消息
        // 添加一个确认监听（只要broker接收到生产者发送的消息，就会回调handleAck方法，否则在一段时间过后会回调handleNack方法）
        channel.addConfirmListener(new ConfirmListener() {

            @Override
            public void handleAck(long deliveryTag, boolean multiple) {
                log.info("ack!");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) {
                log.info("no ack!");
            }
        });

        // 发送一条消息
        String msg = "Hello RabbitMQ send confirm message!";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
    }
}
