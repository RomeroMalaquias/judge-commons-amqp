package br.ufal.ic.judge.commons;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class ConnectionManager {
    private ConnectionFactory factory = new ConnectionFactory();

    private Connection connection;
    private Channel channel;

    public void createConnection() {
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.basicQos(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeConnection() {
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Channel getChannel() {
        return channel;
    }
}
