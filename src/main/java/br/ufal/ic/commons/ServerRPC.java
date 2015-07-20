package br.ufal.ic.commons;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.QueueingConsumer;

public abstract class ServerRPC extends Server {
    private Queue queue;
    private boolean listen = true;
    private QueueingConsumer consumer;

    public ServerRPC(String exchangeName, String key) {
        super(exchangeName);
        queue = new Queue(this.getExchange().getChannel());
        this.getExchange().bindQueue(queue.getName(), key);
        String response;

        try {
            System.out.println("[*] Waiting for messages. To exit press CTRL+C");
            consumer = new QueueingConsumer(this.getExchange().getChannel());
            this.getExchange().getChannel().basicConsume(queue.getName(), false, consumer);
            while (listen) {
                response = "";
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.println(" [x] Received " + message);
                response = doWork(message);
                AMQP.BasicProperties props = delivery.getProperties();
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                        .correlationId(props.getCorrelationId())
                        .build();
                System.out.println("Replying: " + response + " to:" + props.getReplyTo());
                this.getExchange().getChannel().basicPublish( "", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                this.getExchange().getChannel().basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        listen = false;
        this.getExchange().closeConnection();

    }

    public abstract String doWork (String message);

}
