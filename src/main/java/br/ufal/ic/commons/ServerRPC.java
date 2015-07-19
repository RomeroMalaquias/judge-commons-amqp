package br.ufal.ic.commons;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.QueueingConsumer;

public abstract class ServerRPC extends Server {
    private Queue queue;
    public QueueingConsumer consumer;

    public ServerRPC(String exchangeName, String key) {
        super(exchangeName);
        queue = new Queue(this.getExchange().getChannel());
        this.getExchange().bindQueue(queue.getName(), key);
        String response;

        try {
            System.out.println("[*] Waiting for messages. To exit press CTRL+C");
            consumer = new QueueingConsumer(this.getExchange().getChannel());
            this.getExchange().getChannel().basicConsume(queue.getName(), false, consumer);
            while (true) {
                response = "";
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.println(" [x] Received " + message);
                response = doWork(message);
                System.out.println(message);
                AMQP.BasicProperties props = delivery.getProperties();
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                        .correlationId(props.getCorrelationId())
                        .build();
                System.out.println("Replying to:" + props.getReplyTo());
                this.getExchange().getChannel().basicPublish( "", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                Thread.sleep(4000);
                this.getExchange().getChannel().basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract String doWork (String message);

}
