package org.yangxin.rabbitmq.rabbitmqapi.api.message;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil.getConnection;

/**
 * @author yangxin
 * 12/30/20 4:08 PM
 */
@Slf4j
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = getConnection();

        // 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        // 声明一个队列
        String queueName = "test001";
        channel.queueDeclare(queueName, true, false, false, null);

        // 设置Channel
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) {
                String msg = new String(body);
                log.info("消费端：[{}]", msg);

                Map<String, Object> headerMap = properties.getHeaders();
                if (!CollectionUtils.isEmpty(headerMap)) {
                    log.info("my1: [{}}, my2: [{}]", headerMap.get("my1"), headerMap.get("my2"));
                }
            }
        });
    }
}
