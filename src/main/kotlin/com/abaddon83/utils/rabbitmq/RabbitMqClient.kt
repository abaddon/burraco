package com.abaddon83.utils.rabbitmq

import com.abaddon83.utils.configs.Config
import com.rabbitmq.client.*
import java.nio.charset.StandardCharsets

object RabbitMqClient {
    private val factory = ConnectionFactory()
    private val connection: Connection = newConnection();
    private val channel: Channel = getChannel();

    private fun newConnection(): Connection {
        if (connection != null && connection.isOpen) {
            return connection;
        }
        factory.setUsername(Config.getProperty("rabbitmq.client.username"));
        factory.setPassword(Config.getProperty("rabbitmq.client.password"));
        factory.setVirtualHost(Config.getProperty("rabbitmq.client.virtualhost"));
        factory.setHost(Config.getProperty("rabbitmq.client.host"));
        Config.getProperty("rabbitmq.client.port")?.let { factory.setPort(it.toInt()) }

        return factory.newConnection();
    }

    private fun getChannel(): Channel {
        if (channel != null && channel.isOpen) {
            return channel
        }
        return newConnection().createChannel()
    }


    fun sendMessage(queueName: String, message: String) {
        getChannel().queueDeclare(queueName, true, false, false, null);
        getChannel().basicPublish(
                "",
                queueName,
                null,
                message.toByteArray(StandardCharsets.UTF_8)
        )
    }

    fun addListener(queueName: String, consumerTag: String, deliverCallback: DeliverCallback, cancelCallback: CancelCallback) {
        getChannel().basicConsume(queueName, true, consumerTag, deliverCallback, cancelCallback)
    }


}