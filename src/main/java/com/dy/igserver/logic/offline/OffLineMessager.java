package com.dy.igserver.logic.offline;

import com.dy.igserver.utils.MongoUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class OffLineMessager {

    public static void insertOffLineMsg(String user_id,String from_user_id,String typeu,String fingerPrint,String dataContent){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dy");
        Document document = new Document("user_id",user_id)
                .append("user_friend_id",from_user_id).append("typeu",typeu).append("fingerPrint",fingerPrint).append("dataContent",dataContent)
                .append("flag",0);
        collection.insertOne(document);
    }

    public static List<Document> getReceiveMsg(String user_id,int readed){

        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("dy");
        //指定查询过滤器
        Bson afilter = Filters.eq("user_id",user_id);
        Bson bfilter = Filters.eq("flag",readed);
        Bson filter = Filters.and(afilter,bfilter);
        //指定查询过滤器查询
        FindIterable findIterable = collection.find(filter);  //filter
        MongoCursor cursor = findIterable.iterator();
        List<Document> rDocument = new ArrayList<Document>();
        while (cursor.hasNext()) {
            rDocument.add((Document) cursor.next());
        }
        return rDocument;
    }

    public static void updateReceiveMsg(String user_id){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("dy");
        //指定查询过滤器
        Bson afilter = Filters.eq("user_id",user_id);
        Bson bfilter = Filters.eq("flag",0);
        Bson filter = Filters.and(afilter,bfilter);
        //指定查询过滤器查询
        FindIterable findIterable = collection.find(filter);  //filter
        Document document = new Document("$set", new Document("flag", 1));
        collection.updateMany(filter, document);
    }

    public static void updateReceiveMsg(String user_id,String user_friend_id){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("dy");
        //指定查询过滤器
        Bson afilter = Filters.eq("user_id",user_id);
        Bson bfilter = Filters.eq("flag",0);
        Bson cfilter = Filters.eq("user_friend_id",user_friend_id);
        Bson filter = Filters.and(afilter,bfilter,cfilter);
        //指定查询过滤器查询
        FindIterable findIterable = collection.find(filter);  //filter
        Document document = new Document("$set", new Document("flag", 1));
        collection.updateMany(filter, document);
    }

    public static List<Document> getFriendReceiveMsg(String user_id, String user_friend_id, int readed){

        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("dy");
        //指定查询过滤器
        Bson afilter = Filters.eq("user_id",user_id);
        Bson bfilter = Filters.eq("flag",readed);
        Bson cfilter = Filters.eq("user_friend_id",user_friend_id);
        Bson filter = Filters.and(afilter,bfilter,cfilter);
        //指定查询过滤器查询
        FindIterable findIterable = collection.find(filter);
        MongoCursor cursor = findIterable.iterator();
        List<Document> rDocument = new ArrayList<Document>();
        while (cursor.hasNext()) {
            rDocument.add((Document) cursor.next());
        }
        return rDocument;
    }
}
