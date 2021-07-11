package org.acme.getting.started.mongodb;

import java.util.List;
import java.util.Objects;

public class Device {
    private String name;
    private String status;
    private String environment;
    private String user;
    private List<String> repeteadSchedules;
    private List<String> noRepeteadSchedules;
    
    public Device(String name, String status, String environment, String user) {
        this.name = name;
        this.status = status;
        this.environment = environment;
        this.user = user;
    }
    public Device(){}

    public List<String> getRepeteadSchedules() {
        return repeteadSchedules;
    }

    public void setRepeteadSchedules(List<String> repeteadSchedules) {
        this.repeteadSchedules = repeteadSchedules;
    }

    public List<String> getNoRepeteadSchedules() {
        return noRepeteadSchedules;
    }

    public void setNoRepeteadSchedules(List<String> noRepeteadSchedules) {
        this.noRepeteadSchedules = noRepeteadSchedules;
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Device)) {
            return false;
        }

        Device other = (Device) obj;

        return Objects.equals(other.name, this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

}