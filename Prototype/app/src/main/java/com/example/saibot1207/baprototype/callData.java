package com.example.saibot1207.baprototype;

/**
 * Created by saibot1207 on 11.01.16.
 */
public class CallData {

    private int amountCalls;
    private int amountMissed;
    private int amountIncoming;
    private int amountOutgoing;

    private int totalDuration;
    private int averageDuration;

    int messagesAmount;
    int messagesSend;
    int messagesReceived;

    int totalMessageLength;
    int sentMessageLength;
    int receivedMessageLength;

    int averageMessageLength;
    int averageSentMessageLength;
    int averageReceivedMessageLength;



    public CallData() {
        this.amountCalls = 0;
        this.amountIncoming = 0;
        this.amountMissed = 0;
        this.amountOutgoing = 0;

        this.totalDuration = 0;
        this.averageDuration = 0;


        this.messagesAmount = 0;
        this.messagesSend = 0;
        this.messagesReceived = 0;
    }

    public int getAmountCalls() {
        return amountIncoming + amountOutgoing + amountMissed;
    }

    public void setAmountCalls() {
        this.amountCalls = amountIncoming + amountOutgoing + amountMissed;
    }

    public int getAmountMissed() {
        return amountMissed;
    }

    public void setAmountMissed(int amountMissed) {
        this.amountMissed = amountMissed;
        setAmountCalls();
    }

    public int getAmountIncoming() {
        return amountIncoming;
    }

    public void setAmountIncoming(int amountIncoming) {
        this.amountIncoming = amountIncoming;
        setAmountCalls();
    }

    public int getAmountOutgoing() {
        return amountOutgoing;
    }

    public void setAmountOutgoing(int amountOutgoing) {
        this.amountOutgoing = amountOutgoing;
        setAmountCalls();
    }

    public int getMessagesSend() {
        return messagesSend;
    }

    public void setMessagesSend(int messagesSend) {
        this.messagesSend = messagesSend;
    }

    public int getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(int messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public int getAverageDuration() {
        if (getAmountCalls() != amountMissed) {
            return totalDuration / (getAmountCalls() - amountMissed);
        }
        else return 0;
    }

    public int getMessagesAmount() {
        return messagesAmount;
    }

    public void setMessagesAmount(int messagesAmount) {
        this.messagesAmount = messagesAmount;
    }

    public int getTotalMessageLength() {
        return totalMessageLength;
    }

    public void setTotalMessageLength(int totalMessageLength) {
        this.totalMessageLength = totalMessageLength;
    }

    public int getSentMessageLength() {
        return sentMessageLength;
    }

    public void setSentMessageLength(int sentMessageLength) {
        this.sentMessageLength = sentMessageLength;
    }

    public int getReceivedMessageLength() {
        return receivedMessageLength;
    }

    public void setReceivedMessageLength( int receivedMessageLength) {
        this.receivedMessageLength = receivedMessageLength;
    }

    public int getAverageMessageLength() {
        if (messagesAmount != 0){
            return totalMessageLength/messagesAmount;
        }
        else return 0;

    }

    public int getAverageSentMessageLength() {
        if (messagesSend != 0) {
            return sentMessageLength/messagesSend;
        }
        else {
            return 0;
        }
    }

    public int getAverageReceivedMessageLength() {
        if (messagesReceived != 0) {
            return receivedMessageLength/messagesReceived;
        }

        else return 0;

    }
}
