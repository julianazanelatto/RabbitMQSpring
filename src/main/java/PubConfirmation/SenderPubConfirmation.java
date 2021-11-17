package PubConfirmation;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Vector;

public class SenderPubConfirmation {

    private static String NAME_EXCHAGE = "fanoutExchange";

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
            AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
            //System.out.println(selectOk);

            // declarar a fila que será utilizada
            //nome da fila, exclusiva, autodelete, durable, map(args)
            channel.exchangeDeclare(NAME_EXCHAGE,"fanout");

            //criar a mensagem
            Vector<String> message = new Vector<String>(3);
            message.add("Hello world");
            message.add("This is the second message");
            message.add("This is the final message");

            //enviar a mensagem

            for (int i = 0; i < 3; i++) {
                String body = message.get(i);
                channel.basicPublish(NAME_EXCHAGE, "", null, body.getBytes());
                System.out.println("[x] Sending the message: "+body);
                // wait for 5 sec.
                channel.waitForConfirmsOrDie(5_000);
                System.out.println("[v] Message confirmed ");
            }

            System.out.print("[x] Done'");
        }
    }
}
