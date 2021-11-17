package PubConfirmation;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Vector;

public class SecondSenderPubConfirmation {

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
            String message = "This is my ";
            int setOfMessages = 10;
            int outMessages = 0;
            String bodyMessage;

            //enviar a mensagem
            for (int i = 0; i < setOfMessages; i++) {
                bodyMessage = message+i;
                channel.basicPublish(NAME_EXCHAGE, "", null, bodyMessage.getBytes());
                System.out.println("[x] Sending the message: "+bodyMessage);
                outMessages++;

                if (outMessages == setOfMessages){
                    // wait for 5 sec.
                    channel.waitForConfirmsOrDie(5_000);
                    System.out.println("[v] Message confirmed ");
                    outMessages = 0;
                }

            }
            if (outMessages != 0){
                System.out.println();
                channel.waitForConfirmsOrDie(5_000);
                System.out.println("[v] Message confirmed ");
            }
            System.out.print("[x] Done'");
        }
    }
}
