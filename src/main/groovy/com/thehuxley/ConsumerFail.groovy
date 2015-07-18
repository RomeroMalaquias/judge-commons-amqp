package com.thehuxley

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.QueueingConsumer

/**
 * Created by marcio on 18/07/15.
 */
class ConsumerFail {

    def static main(args) throws IOException, InterruptedException{
        ConnectionFactory factory = new ConnectionFactory()
        factory.setHost("localhost")

        while (true) {
            Connection connection = factory.newConnection()
            Channel channel = connection.createChannel();

            def prefetchCount = 1

            channel.exchangeDeclare("NOSSO_EXCHANGE2", 'direct')
            def queue = channel.queueDeclare().queue


            channel.queueBind(queue, "NOSSO_EXCHANGE2", "")


            channel.basicQos(prefetchCount)

            println " [*] Waiting for messages. To exit press CTRL+C"

            QueueingConsumer consumer = new QueueingConsumer(channel)
            channel.basicConsume(queue, false, consumer)


            QueueingConsumer.Delivery delivery = consumer.nextDelivery()
            String message = new String(delivery.body)
            println(" [x] Received '$message'")
            Thread.sleep(4000)
            channel.close()
            connection.close()
        }
    }

}
