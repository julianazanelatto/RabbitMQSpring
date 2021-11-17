package WorkQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderWork {

    private static String NAME_QUEUE = "Work";

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
            channel.queueDeclare(NAME_QUEUE, false, false, false, null);

            //criar a mensagem
            String message = ".......";

            //enviar a mensagem
            channel.basicPublish("", NAME_QUEUE, null, message.getBytes());

            System.out.print("[x] Sent '" + message + "'");
        }
    }
}
