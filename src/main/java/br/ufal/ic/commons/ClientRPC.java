package br.ufal.ic.commons;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.QueueingConsumer;


public abstract class ClientRPC extends Client {
    public String replyQueueName;
    public Queue replyQueue;
    public boolean listen = true;

    public ClientRPC(String exchangeName, String key) {
        super(exchangeName, key);

    }

    public ClientRPC(String exchangeName, String queueName, String key) {
        super(exchangeName, queueName, key);

    }

    public void call(String message) throws Exception {

        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .replyTo(replyQueueName)
                .build();
        queue.getChannel().basicPublish(this.getExchangeName(), this.getKey(), props, message.getBytes("UTF-8"));
        System.out.println(" [x] Published message " + message);
    }

    public void close() {
        exchange.closeConnection();
        replyQueue.closeConnection();
        listen = false;

    }

    public void start() {
        try {
            if (replyQueueName == null) {
                replyQueue = new Queue(queue.getChannel());
                replyQueueName = replyQueue.getName();
                System.out.println(" [x] Waiting messages on " + replyQueueName);
                Thread thread = new Thread() {
                    public void run(){

                        try {
                            consumer = new QueueingConsumer(queue.getChannel());
                            queue.getChannel().basicConsume(replyQueueName, false, consumer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        while (listen) {
                            try {
                                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                                System.out.println(" [x] Received " + new String(delivery.getBody(), "UTF-8"));
                                doWork(new String(delivery.getBody(), "UTF-8"));
                                queue.getChannel().basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
