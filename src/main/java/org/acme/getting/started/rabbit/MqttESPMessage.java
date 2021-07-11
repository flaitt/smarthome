package org.acme.getting.started.rabbit;

public class MqttESPMessage {
    private String action;
    private String name;
    private String status;
    private String environment;
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "MqttESPMessage [action=" + action + ", environment=" + environment + ", name=" + name + ", status="
                + status + ", user=" + user + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;

    public MqttESPMessage(){}
}