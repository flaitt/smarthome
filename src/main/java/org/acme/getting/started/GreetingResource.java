package org.acme.getting.started;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;

import org.acme.getting.started.esp8266.Esp8266Controller;
import org.acme.getting.started.esp8266.Esp8266Response;
import org.acme.getting.started.mongodb.Device;
import org.acme.getting.started.mongodb.Environment;
import org.acme.getting.started.mongodb.House;
import org.acme.getting.started.mongodb.SmarthomeService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/")
public class GreetingResource {
    @Inject SmarthomeService service;
    @Inject
    @RestClient
    Esp8266Controller esp8266Controller;

    private final static String QUEUE_NAME = "Sender";
    String topic        = "Sender"; //routing-key
    // String content      = "L";
    int qos             = 1;
    String broker       = "tcp://snake.rmq2.cloudamqp.com:1883";
    String clientId     = "amq.topic"; //para dar o nome para fila
    MemoryPersistence persistence = new MemoryPersistence();

    @POST
    @Path("toggleStatus")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@QueryParam("action") String action, @QueryParam("routingKey") String routingKey) {
        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("zylppjqz:zylppjqz");
            connOpts.setPassword(new char[]{'b','x','e','7','K','t','s','z','i','3','5','K','O','9','-','4','R','H','h','P','2','p','V','h','y','p','i','P','W','V','k','2'});
            mqttClient.connect(connOpts);
            MqttMessage message = new MqttMessage(action.getBytes());
            message.setQos(qos);
            System.out.println("Publish message: " + message);
            mqttClient.publish(routingKey, message);

            System.out.println(" [x] Sent '" + message + "'");

        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
        return "ok toggle";
    }

    @Path("adddevice")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDeviceToDatabase(@QueryParam("name") String name,
                                @QueryParam("status") String status,
                                @QueryParam("environment") String environment,
                                @QueryParam("user") String user) {
        Device device = new Device(name, status, environment, user);
        try {
            service.addDevice(device);
            return Response
                        .ok()
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("ok")
                        .build();
        } catch (Exception e) {
            System.out.println("Erro ao aidicionar objeto no mongo");
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("addenvironment")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addEnvironemntToDatabase(@QueryParam("name") String name,
                                @QueryParam("house") String house,
                                @QueryParam("user") String user) {
        Environment environment = new Environment(name, house, user);
        try {
            service.addEnvironment(environment);
        } catch (Exception e) {
            System.out.println("Erro ao adicionar objeto no mongo");
        }
        return "ok environment";
    }

    @Path("addhouse")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addHouseToDatabase(@QueryParam("name") String name,
                                @QueryParam("user") String user) {
        House house = new House(name, user);
        try {
            service.addHouse(house);
        } catch (Exception e) {
            System.out.println("Erro ao adicionar objeto no mongo");
        }
        return "ok house";
    }

    @Path("connect/esp8266")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response connectEsp8266(@QueryParam("rede") String rede,
                                   @QueryParam("senha") String senha) {
        try {
            System.out.println("Acessou o resource connect esp");
            Esp8266Response esp8266Response = esp8266Controller.connectEsp(rede, senha);
            return Response
                        .ok()
                        .header("Access-Control-Allow-Origin", "*")
                        .entity(esp8266Response)
                        .build();
        } catch (Exception e) {
            System.out.println("falha em connect esp");
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @Path("publish")
    // @GET
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.TEXT_PLAIN)
    // public String publishESP8266(@QueryParam("message") String message) {
    //     ConnectionFactory factory = new ConnectionFactory();
    //     factory.setHost("localhost");
    //     factory.setUsername("guest");
    //     factory.setPassword("guest");

    //     try {
    //         Connection connection = factory.newConnection();
    //         Channel channel = connection.createChannel();
    //         channel.basicPublish("test-exchange", "inTopic", null, message.getBytes());
    //         return "[SUCESSO] Mensagem enviada para ESP8266";
    //     } catch (Exception e) {
    //         return "[ERRO] Mensagem não enviada para ESP8266";
    //     }
    // }

    @Path("device")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response devices(@QueryParam("user") String user, @QueryParam("environment") String environment) {
        List<Device> devices = new ArrayList<>();
        System.out.println("obtendo a lista de dispositovos");
        try {
            devices = service.getDevicesByUserAndEnvironment(user, environment);
            System.out.println("obtendo a lista de dispositovos2");
            return Response.ok().header("Access-Control-Allow-Origin", "*").entity(devices).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("environment")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response environment(@QueryParam("user") String user, @QueryParam("house") String house) {
        List<Environment> environments = new ArrayList<>();

        try {
            environments = service.getEnvironmentByUserAndHouse(user, house);
            return Response.ok().entity(environments).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("house")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response houses(@QueryParam("user") String user) {
        List<House> houses = new ArrayList<>();

        try {
            houses = service.getHouseByUser(user);
            
            return Response.ok().entity(houses).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }


}