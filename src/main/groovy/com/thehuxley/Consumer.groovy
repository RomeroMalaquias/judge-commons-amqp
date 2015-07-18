package com.thehuxley

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.QueueingConsumer

class Consumer {

    def static main(args) throws IOException, InterruptedException{
        ConnectionFactory factory = new ConnectionFactory()
        factory.setHost("localhost")
        Connection connection = factory.newConnection()
        Channel channel = connection.createChannel();

        def prefetchCount = 1

        channel.exchangeDeclare("NOSSO_EXCHANGE2", 'direct')
        channel.queueDeclare("NOSSA_QUEUE", true, false, false, null)


        channel.queueBind("NOSSA_QUEUE", "NOSSO_EXCHANGE2", "evaluator")


        channel.basicQos(prefetchCount)

        println " [*] Waiting for messages. To exit press CTRL+C"

        QueueingConsumer consumer = new QueueingConsumer(channel)
        channel.basicConsume("NOSSA_QUEUE", false, consumer)

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery()
            String message = new String(delivery.body)
            println(" [x] Received '$message'")
            Thread.sleep(4000)
            channel.basicAck(delivery.envelope.deliveryTag, false)
        }
    }

}
