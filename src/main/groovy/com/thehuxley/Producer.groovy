package com.thehuxley

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

class Producer {

    def static main(args) {
        ConnectionFactory factory = new ConnectionFactory()
        factory.setHost("localhost")
        Connection connection = factory.newConnection()
        Channel channel = connection.createChannel()

        def prefetchCount = 1


        channel.exchangeDeclare("NOSSO_EXCHANGE2", "direct")
        channel.basicQos(prefetchCount)

        String message = "Hello World!"

        (1..19).each {
            channel.basicPublish("NOSSO_EXCHANGE2", "evaluator", null, (message + it).bytes)
        }

        println " [X] Sent '$message'"

        channel.close()
        connection.close()
    }
}