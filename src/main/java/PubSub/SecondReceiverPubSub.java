package PubSub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class SecondReceiverPubSub {
    private static String NAME_QUEUE = "broadcast";
    private static String NAME_EXCHANGE = "fanoutExchange";

    public static void main(String[] args0) throws Exception{
        //primeiro criar a conexão
        //setar as informações para cria-la
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.24.0.2");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        Connection connection = factory.newConnection();
        //System.out.println(connection.hashCode());

        // criar um novo canal
        Channel channel = connection.createChannel();
        System.out.println(channel);
        //nome da fila, exclusiva, autodelete, durable, map(args)
        channel.queueDeclare(NAME_QUEUE,false, false, false, null);

        //declaração da exchange
        channel.exchangeDeclare(NAME_EXCHANGE,"fanout");
        channel.queueBind(NAME_QUEUE,NAME_EXCHANGE,"");

        DeliverCallback deliverycallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(),"UTF-8");
            System.out.println("[*] Received message: '"+ message + "'");
        };

        channel.basicConsume(NAME_QUEUE, true, deliverycallback, ConsumerTag->{});
    }
}
