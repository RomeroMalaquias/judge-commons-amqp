package br.ufal.ic.commons;

public class Server {
    private Exchange exchange;
    private String exchangeName;

    public Server(String exchangeName) {
        this.exchangeName = exchangeName;
        exchange = new Exchange(this.exchangeName);
    }
    public Exchange getExchange() {
        return exchange;
    }
    public String getExchangeName() {
        return exchangeName;
    }

    public void publish (String message, String key) {
        try {
            exchange.getChannel().basicPublish(exchange.getName(), key, null, message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        exchange.closeConnection();
    }
}
