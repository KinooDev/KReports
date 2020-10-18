package com.kino.kreports.data.storage.impl.mongo;

import com.kino.kreports.data.KReportStorage;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBStorage implements KReportStorage {

    private MongoDatabase database;
    private MongoCollection<Document> collection;
}
