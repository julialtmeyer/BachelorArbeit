package de.htwsaar.backend_service.mqtt.messages;

import java.util.Objects;

public class Message {
    String messageContent;
    String topic;
    Integer qos;

    public Message(String messageContent, String topic, Integer qos) {
        this.messageContent = messageContent;
        this.topic = topic;
        this.qos = qos;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageContent='" + messageContent + '\'' +
                ", topic='" + topic + '\'' +
                ", qos=" + qos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(messageContent, message.messageContent) && Objects.equals(topic, message.topic) && Objects.equals(qos, message.qos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageContent, topic, qos);
    }
}
