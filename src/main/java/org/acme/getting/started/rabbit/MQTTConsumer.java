package org.acme.getting.started.rabbit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.getting.started.mongodb.Device;
import org.acme.getting.started.mongodb.SmarthomeService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.runtime.Startup;

@Startup
@ApplicationScoped
public class MQTTConsumer {
    @Inject SmarthomeService service;

    MQTTConsumer() {
        String topic        = "javaTopic"; //routing-key
        String content      = "Hello CloudAMQP";
        int qos             = 1;
        String broker       = "tcp://snake.rmq2.cloudamqp.com:1883";

        //MQTT client id to use for the device. "" will generate a client id automatically
        String clientId     = "JavaBackend"; //para dar o nome para fila

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            System.out.println("trying to connect");
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            System.out.println("client instancied");
            mqttClient.setCallback(new MqttCallback() {
              public void messageArrived(String topic, MqttMessage msg)
                throws Exception {
                  System.out.println("Recived:" + topic);
                  System.out.println("Recived:" + new String(msg.getPayload()));

                  ObjectMapper mapper = new ObjectMapper();
                  MqttESPMessage message = mapper.readValue(new String(msg.getPayload()), MqttESPMessage.class);
                  System.out.println("Recebeu mensagem" + message.toString());
                  if (message.getAction().equals("newDevice")) {
                    Device device = new Device(message.getName(), message.getStatus(), message.getEnvironment(), message.getUser());
                    service.addDevice(device);
                  }
                  if (message.getAction().equals("updateStatus")) {
                    System.out.println("atualizando status" + message.toString());
                    service.updateDevice(message.getUser(), message.getEnvironment(), message.getName(), message.getStatus());
                  }
                }
                

              public void deliveryComplete(IMqttDeliveryToken arg0) {
                System.out.println("Delivary complete");
              }

              public void connectionLost(Throwable arg0) {
                // TODO Auto-generated method stub
              }
            });
            System.out.println("calback created");
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("zylppjqz:zylppjqz");
            connOpts.setPassword(new char[]{'b','x','e','7','K','t','s','z','i','3','5','K','O','9','-','4','R','H','h','P','2','p','V','h','y','p','i','P','W','V','k','2'});
            mqttClient.connect(connOpts);
            mqttClient.subscribe(topic, qos);
            System.out.println("subscribed");
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}
