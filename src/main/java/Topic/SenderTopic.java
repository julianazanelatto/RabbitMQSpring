package Topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderTopic {

    private static String NAME_EXCHAGE = "TopicExchange";

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
            channel.exchangeDeclare(NAME_EXCHAGE,"topic");

            //criar a mensagem
            String routingKey = "quick.orange.rabbit";
            String message = "Hello! This is original message!";
            String routingKey1 = "quick.rabbit";
            String message1 = "Hello! This is the message with the routing key:"+ routingKey1;
            String routingKey2 = "rabbit.orange";
            String message2 = "Hello! This is the message with the routing key:"+ routingKey2;


            //enviar a mensagem
            channel.basicPublish(NAME_EXCHAGE, routingKey, null, message.getBytes());
            channel.basicPublish(NAME_EXCHAGE, routingKey1, null, message1.getBytes());
            channel.basicPublish(NAME_EXCHAGE, routingKey2, null, message2.getBytes());

            System.out.print("[x] Sent '" + message + "'");
        }
    }
}
