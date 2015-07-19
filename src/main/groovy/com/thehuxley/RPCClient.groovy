package com.thehuxley

import br.ufal.ic.commons.ClientRPC
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.QueueingConsumer

class EvaluatorRPC extends ClientRPC {
    EvaluatorRPC(String exchangeName, String queueName, String key) {
        super(exchangeName, queueName, key);
    }

    void doWork (String message){
        println "ok";
    }
}


public class RPCClient {


    public static void main(String[] argv) {
        EvaluatorRPC fibonacciRpc = new EvaluatorRPC("NOSSO_EXCHANGE2", "NOSSA_QUEUE", "evaluator");
        fibonacciRpc.start();
        fibonacciRpc.call('teste');
        fibonacciRpc.call('teste2');
        fibonacciRpc.call('teste3');

    }
}
