package org.yangxin.rabbitmq.rabbitmqapi.api.returnlistener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;
import lombok.extern.slf4j.Slf4j;
import org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yangxin
 * 12/31/20 6:47 PM
 */
@Slf4j
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";

        channel.addReturnListener((replyCode, replyText, exchange, routingKey1, properties, body) -> {
            log.info("handle return...");
            log.info("replyCode: [{}], replyText: [{}], exchange: [{}], routingKey1: [{}], properties: [{}], body: [{}]",
                    replyCode, replyText, exchange, routingKey1, properties, new String(body));
        });

        String msg = "Hello RabbitMQ Return Message";
        /*
            mandatory：如果为true，则监听器会接收到路由不可达的消息，然后进行后续处理（也就是说broker会保存这条不可路由的消息），
            如果为false，那么broker端自动删除该消息！
         */
//        channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
        channel.basicPublish(exchangeName, routingKeyError, true, null, msg.getBytes());
    }
}
