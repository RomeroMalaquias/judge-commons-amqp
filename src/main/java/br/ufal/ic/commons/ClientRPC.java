package br.ufal.ic.commons;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.QueueingConsumer;


public abstract class ClientRPC extends Client {
    public String replyQueueName;

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

    public void start() {
        try {
            if (replyQueueName == null) {
                replyQueueName = queue.getChannel().queueDeclare().getQueue();
                System.out.println(" [x] Waiting messages on " + replyQueueName);
                Thread thread = new Thread() {
                    public void run(){
                        try {
                            System.out.println("Runing thread");
                            consumer = new QueueingConsumer(queue.getChannel());
                            queue.getChannel().basicConsume(replyQueueName, true, consumer);
                            while (true) {

                                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                                    System.out.println(" [x] Received " + new String(delivery.getBody(), "UTF-8"));
                                    doWork(new String(delivery.getBody(), "UTF-8"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
