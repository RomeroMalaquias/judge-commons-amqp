package br.ufal.ic.commons;

import com.rabbitmq.client.QueueingConsumer;


public abstract class Client {
    public Exchange exchange;
    public String exchangeName;
    public Queue queue;
    public QueueingConsumer consumer;
    public String key;

    public Client(String exchangeName, String queueName, String key) {
        this.exchangeName = exchangeName;
        exchange = new Exchange(exchangeName);
        queue = new Queue(queueName);
        exchange.bindQueue(queue.getName(), key);
        this.key = key;
    }

    public Client(String exchangeName, String key) {
        this.exchangeName = exchangeName;
        exchange = new Exchange(exchangeName);
        queue = new Queue(this.getExchange().getChannel());
        exchange.bindQueue(queue.getName(), key);
        this.key = key;
    }

    public void start() {
        try {
            System.out.println("[*] Waiting for messages. To exit press CTRL+C");
            consumer = new QueueingConsumer(exchange.getChannel());
            exchange.getChannel().basicConsume(queue.getName(), false, consumer);
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.println(" [x] Received " + message);
                doWork(message);
                Thread.sleep(4000);
                exchange.getChannel().basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        exchange.closeConnection();
    }
    public Exchange getExchange() {
        return exchange;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getKey() {
        return key;
    }

    public abstract void doWork (String message);
}
