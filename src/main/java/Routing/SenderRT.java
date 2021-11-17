package Routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderRT {
    private static String ROUTING_KEY = "routingKeyTest";
    private static String ROUTING_KEY_A = "secondRoutingKeyTest";
    private static String NAME_EXCHAGE = "directExchange";

    public static void main(String[] args0) throws Exception{
        //primeiro criar a conexão
        //setar as informações para cria-la
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.24.0.2");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        try( Connection connection = factory.newConnection()) {
            //System.out.println(connection.hashCode());

            // criar um novo canal
            Channel channel = connection.createChannel();
            System.out.println(channel);

            // declarar a fila que será utilizada
            //nome da fila, exclusiva, autodelete, durable, map(args)
            channel.exchangeDeclare(NAME_EXCHAGE,"direct");

            //criar a mensagem
            String message = "Hello! This is a RabbitMQ system!";
            String secondMessage = "Hello! This is a routing key based system!";

            //enviar a mensagem
            channel.basicPublish(NAME_EXCHAGE, ROUTING_KEY, null, message.getBytes());
            channel.basicPublish(NAME_EXCHAGE, ROUTING_KEY_A, null, secondMessage.getBytes());

            System.out.print("[x] Sent '" + message + "'");
        }
    }
}
