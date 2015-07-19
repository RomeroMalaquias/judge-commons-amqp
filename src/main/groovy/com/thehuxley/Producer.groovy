package com.thehuxley

import br.ufal.ic.commons.Server

class Producer {

    def static main(args) {
        Server producer = new Server("NOSSO_EXCHANGE2");

        String message = "Hello World!"

        (1..19).each {
            producer.publish(message + it, "evaluator")
            println " [X] Sent '$message$it'"
        }



        producer.close();
    }
}