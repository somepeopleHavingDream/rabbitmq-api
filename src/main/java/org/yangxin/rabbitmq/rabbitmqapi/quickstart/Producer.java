package org.yangxin.rabbitmq.rabbitmqapi.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.yangxin.rabbitmq.rabbitmqapi.util.ConnectionUtil.getConnection;

/**
 * @author yangxin
 * 12/30/20 3:20 PM
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = getConnection();

        // 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        // 通过Channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "Hello RabbitMQ!";
            channel.basicPublish("", "test001", null, msg.getBytes());
        }

        // 记得要关闭相关的连接
        channel.close();
        connection.close();
    }
}
