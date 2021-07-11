package org.acme.getting.started.mongodb;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;

@ApplicationScoped
public class SmarthomeService {

    @Inject MongoClient mongoClient;

    // public List<Device> list(){
    //     List<Device> list = new ArrayList<>();
    //     Bson filter = and(eq("a", "a"),eq("b", "b"));

    //     MongoCursor<Document> cursor = getCollection("device").find().iterator();

    //     try {
    //         while(cursor.hasNext()) {
    //             Document document = cursor.next();
    //             Device device = new Device();
    //             device.setName(document.getString("name"));
    //             device.setEnvironment(document.getString("environment"));
    //             device.setNoRepeteadSchedules(document.getList("noRepeatedSchedules", String.class));
    //             device.setRepeteadSchedules(document.getList("RepeatedSchedules", String.class));
    //             device.setStatus(document.getString("status"));
    //             list.add(device);

    //         }
    //     } catch (Exception e) {
    //         //TODO: handle exception
    //     } finally {
    //         cursor.close();
    //     }
    //     return list;
    // }

    public void updateDevice(String user, String environment, String name, String status) {
        Bson filter = and(eq("user", user), eq("environment", environment), eq("name", name));
        
        try {
            getCollection("device").updateOne(filter, new Document("$set", new Document("status", status)));

        } catch (Exception e) {
            System.out.println("errou ao atualizar device");
        }
    }

    public List<Device> getDevicesByUserAndEnvironment(String user, String environment) {
        List<Device> list = new ArrayList<>();
        Bson filter = and(eq("user", user), eq("environment", environment));

        MongoCursor<Document> cursor = getCollection("device").find(filter).iterator();

        try {
            while(cursor.hasNext()) {
                Document document = cursor.next();
                Device device = new Device();
                device.setName(document.getString("name"));
                device.setEnvironment(document.getString("environment"));
                device.setNoRepeteadSchedules(document.getList("noRepeatedSchedules", String.class));
                device.setRepeteadSchedules(document.getList("RepeatedSchedules", String.class));
                device.setStatus(document.getString("status"));
                list.add(device);
            }
        } catch (Exception e) {
            System.out.println("Erro ao obter os devices");
        } finally {
            cursor.close();
        }
        return list;
    }

    public List<Environment> getEnvironmentByUserAndHouse(String user, String house) {
        List<Environment> list = new ArrayList<>();
        Bson filter = and(eq("user", user), eq("house", house));

        MongoCursor<Document> cursor = getCollection("environment").find(filter).iterator();

        try {
            while(cursor.hasNext()) {
                Document document = cursor.next();
                Environment environment = new Environment(document.getString("name"),
                                                          document.getString("house"),
                                                          document.getString("user"));
                list.add(environment);
            }
        } catch (Exception e) {
            System.out.println("Erro ao obter os environments");
        } finally {
            cursor.close();
        }
        return list;
    }

    public List<House> getHouseByUser(String user) {
        List<House> list = new ArrayList<>();
        Bson filter = eq("user", user);

        MongoCursor<Document> cursor = getCollection("house").find(filter).iterator();

        try {
            while(cursor.hasNext()) {
                Document document = cursor.next();
                House house = new House(document.getString("name"), document.getString("user"));
                list.add(house);
            }
        } catch (Exception e) {
            System.out.println("Erro ao obter house");
        } finally {
            cursor.close();
        }
        return list;
    }

    public void addDevice(Device device) {
        Document document = new Document()
            .append("name", device.getName())
            .append("environment", device.getEnvironment())
            .append("user", device.getUser())
            .append("status", device.getStatus());
        getCollection("device").insertOne(document);
    }

    public void addEnvironment(Environment environment) {
        Document document = new Document()
            .append("name", environment.getName())
            .append("house", environment.getHouse())
            .append("user", environment.getUser());
        getCollection("environment").insertOne(document);
    }

    public void addHouse(House house) {
        Document document = new Document()
            .append("name", house.getName())
            .append("user", house.getUser());
        getCollection("house").insertOne(document);
    }

    private MongoCollection getCollection(String collection) {
        return mongoClient.getDatabase("smarthome").getCollection(collection);
    }
    
}
