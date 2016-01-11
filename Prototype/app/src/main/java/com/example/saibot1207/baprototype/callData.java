package com.example.saibot1207.baprototype;

/**
 * Created by saibot1207 on 11.01.16.
 */
public class CallData {

    int amountCalls;
    int amountMissed;
    int amountIncoming;
    int amountOutgoing;



    int totalDuration;
    int averageDuration;

    int messagesSend;
    int messagesReceived;



    public CallData() {
        this.amountCalls = 0;
        this.amountIncoming = 0;
        this.amountMissed = 0;
        this.amountOutgoing = 0;

        this.totalDuration = 0;
        this.averageDuration = 0;


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
        return totalDuration / (getAmountCalls() - amountMissed);
    }
}
