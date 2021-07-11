package org.acme.getting.started.esp8266;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Esp8266Response {
    public String status;
}
