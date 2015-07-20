package com.thehuxley

import br.ufal.ic.commons.ClientRPC

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
        fibonacciRpc.call('{code:"println \'oi\'", language: "groovy", input: "2", output: "3"}');
        fibonacciRpc.call('{code:"println \'oi\'", language: "groovy", input: "2", output: "3"}');
        fibonacciRpc.call('{code:"println \'oi\'", language: "groovy", input: "2", output: "3"}');

    }
}
