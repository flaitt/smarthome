// package org.acme.getting.started.rabbit;

// import javax.enterprise.context.ApplicationScoped;

// import com.rabbitmq.client.Channel;
// import com.rabbitmq.client.Connection;
// import com.rabbitmq.client.ConnectionFactory;
// import com.rabbitmq.client.DeliverCallback;

// import io.quarkus.runtime.Startup;

// @Startup
// @ApplicationScoped
// public class Consumer {
//     private final static String QUEUE_NAME = "hello";

//     Consumer() {
//         try {
//             ConnectionFactory factory = new ConnectionFactory();
            
//             factory.setHost("localhost");
//             factory.setUsername("guest");
//             factory.setPassword("guest");
    
//             Connection connection = factory.newConnection();
//             Channel channel = connection.createChannel();
    
//             channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    
//             System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            
//             DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//                 String message = new String(delivery.getBody(), "UTF-8");
//                 System.out.println("[x] Received '" + message + "'");
//             };

//             channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    
//         } catch (Exception e) {
//             System.out.println("Deu ruim no consumer");
//         }
//     }
// }
