package com.thehuxley

import br.ufal.ic.commons.ServerRPC

class EvaluatorServer extends ServerRPC {
    EvaluatorServer(String exchangeName, String key) {
        super(exchangeName, key)
    }

    String doWork (String message){
        println "ok";
        return message;
    }
}

public class RPCServer {


    public static void main(String[] argv) {
        EvaluatorServer evaluatorServer = new EvaluatorServer("NOSSO_EXCHANGE2", "evaluator");


    }
}
