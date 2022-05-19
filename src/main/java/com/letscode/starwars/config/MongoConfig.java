package com.letscode.starwars.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
public class MongoConfig extends AbstractReactiveMongoConfiguration {

  @Value("{mongo.uri}")
  String uriMongoDb;

  @Override
  public MongoClient reactiveMongoClient() {
    return MongoClients.create(uriMongoDb);
  }

  @Override
  protected String getDatabaseName() {
    return "rebels";
  }

}
