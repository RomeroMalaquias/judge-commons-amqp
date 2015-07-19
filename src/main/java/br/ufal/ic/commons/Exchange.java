package br.ufal.ic.commons;

public class Exchange extends ConnectionManager {
    private String exchangeName;
    public Exchange(String exchangeName) {
        this.createConnection();
        this.exchangeName = exchangeName;
        try {
            this.getChannel().exchangeDeclare(this.exchangeName, "direct");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getName() {
        return exchangeName;
    }

    public String toString() {
        return exchangeName;
    }
    public void bindQueue(String queueName, String key) {
        try {
            this.getChannel().queueBind(queueName, exchangeName, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
