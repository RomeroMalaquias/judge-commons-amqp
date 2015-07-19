package com.thehuxley

import br.ufal.ic.commons.Client

class Evaluator extends Client {
    Evaluator(String exchangeName, String queueName, String key) {
        super(exchangeName, queueName, key)
    }

    void doWork (String message){
        println "ok";
    }
}

class Consumer {

    def static main(args) throws IOException, InterruptedException {
        Evaluator evaluator = new Evaluator("NOSSO_EXCHANGE2", "NOSSA_QUEUE", "evaluator");
        evaluator.start();
    }

}
