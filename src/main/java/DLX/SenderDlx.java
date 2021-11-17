package DLX;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Vector;

public class SenderDlx {

    private static String NAME_EXCHAGE = "mainExchange";

    public static void main(String[] args0) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.24.0.2");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        try( Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(NAME_EXCHAGE,"topic");

            //criar a mensagem
            String message = "Hello! This is a test!";
            String routingkey = "bkConsumer.consumer";
            channel.basicPublish(NAME_EXCHAGE, routingkey, null, message.getBytes());

            System.out.print("[x] Done'");
        }
    }
}
