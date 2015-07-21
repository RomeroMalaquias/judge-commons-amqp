package br.ufal.ic.judge.commons;
import com.rabbitmq.client.Channel;

public class Queue extends ConnectionManager {
    private String queueName;
    public Queue(Channel channel) {
        this.createConnection();
        try {
            this.queueName = channel.queueDeclare().getQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Queue(Channel channel, String queueName) {
        this.queueName = queueName;
        try {
            channel.queueDeclare(this.queueName, true, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Queue(String queueName) {
        this.createConnection();
        this.queueName = queueName;
        try {
            this.getChannel().queueDeclare(this.queueName, true, false, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return queueName;
    }

    public String toString() {
        return queueName;
    }
}
