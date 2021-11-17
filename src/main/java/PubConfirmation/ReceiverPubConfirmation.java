package PubConfirmation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiverPubConfirmation {
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

        //o servidor irá determinar um nome randomico para esta fila
        //consequentemente ela será temporária
        String nameQueue = channel.queueDeclare().getQueue();

        // declarar a fila que será utilizada
        //nome da fila, exclusiva, autodelete, durable, map(args)
        //channel.queueDeclare(nameQueue, false, false, false, null);

        //declaração da exchange
        channel.exchangeDeclare(NAME_EXCHANGE,"fanout");
        channel.queueBind(nameQueue,NAME_EXCHANGE,"");

        DeliverCallback deliverycallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(),"UTF-8");
            System.out.println("[*] Received message: '"+ message + "'");
        };

        channel.basicConsume(nameQueue, true, deliverycallback, ConsumerTag->{});
    }
}
