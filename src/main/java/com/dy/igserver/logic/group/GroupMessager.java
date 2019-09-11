package com.dy.igserver.logic.group;

import com.dy.igserver.logic.vo.GroupVO;
import com.dy.igserver.logic.vo.UserVO;
import com.dy.igserver.utils.MongoUtil;
import com.dy.igserver.utils.UniqueID;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class GroupMessager {

    public static Document addGroup(String admin_id, String [] user_ids){

        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");

        GroupVO groupVO = new GroupVO();

        UniqueID idWorker = new UniqueID(1, 0);
        long gid = idWorker.nextId(9);
        String groupId="DYGROUP"+String.valueOf(gid);

        groupVO.setGroupId(groupId);
        groupVO.setAdminId(admin_id);
        groupVO.setGroupName("我创建的群"+groupId.substring(8,14));
        List<UserVO> userVOList =new ArrayList<UserVO>();
        List<String> userIDList =new ArrayList<String>();
        List<String> userNickNameList =new ArrayList<String>();
        userIDList.add(admin_id);
        userNickNameList.add("-");
        for(int i=0;i<user_ids.length;i++){
            UserVO userVO =  new UserVO();
            userVO.setUserId(user_ids[i]);
            userVOList.add(userVO);
            userIDList.add(user_ids[i]);
            userNickNameList.add("-");
        }
        groupVO.setUserVOList(userVOList);

        Document document = new Document("group_id",groupId)
                .append("adminid",groupVO.getAdminId())
                .append("groupname",groupVO.getGroupName())
                .append("groupnotice",".")
                .append("users",userIDList)
                .append("usersnickname",userNickNameList)
                .append("flag",0)
                .append("groupurl","http://");

        collection.insertOne(document);

        return document;
    }

    public static Document getGroupByid(String group_id){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.eq("group_id",group_id);
        FindIterable findIterable = collection.find(filter);
        MongoCursor cursor = findIterable.iterator();
        Document rDocument= new Document();
        while (cursor.hasNext()) {
            rDocument=(Document) cursor.next();
        }
        return rDocument;
    }

    public static void updateGroupName(String group_id,String group_name){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.eq("group_id",group_id);
        FindIterable findIterable = collection.find(filter);
        Document document = new Document("$set", new Document("groupname", group_name));
        collection.updateOne(filter, document);
    }

    public static void updateGroupUrl(String group_id,String url){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.eq("group_id",group_id);
        FindIterable findIterable = collection.find(filter);
        Document document = new Document("$set", new Document("groupurl", url));
        collection.updateOne(filter, document);
    }

    public static void updateGroupNotice(String group_id,String notice){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.eq("group_id",group_id);
        FindIterable findIterable = collection.find(filter);
        Document document = new Document("$set", new Document("groupnotice", notice));
        collection.updateOne(filter, document);
    }

    public static void updateGroupAdmin(String group_id,String admin_id){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.eq("group_id",group_id);
        FindIterable findIterable = collection.find(filter);
        Document document = new Document("$set", new Document("adminid", admin_id));
        collection.updateOne(filter, document);

    }

    public static void joinGroup(String group_id,String [] user_ids){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.eq("group_id",group_id);
        FindIterable findIterable = collection.find(filter);
        Document rDocument= new Document();
        MongoCursor cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            rDocument=(Document) cursor.next();
        }
        List<String> users=( List<String>)rDocument.get("users");
        List<String> usersNickname=( List<String>)rDocument.get("usersnickname");
        for(int i=0;i<user_ids.length;i++){
            boolean isInGroup=false;
            for(int j=0;j< users.size();j++){
                if(user_ids[i].equals(users.get(j))){
                    isInGroup=true;
                }
            }
            if(!isInGroup) {
                users.add(user_ids[i]);
                usersNickname.add("-");
            }
        }
        Document document = new Document("$set", new Document("users", users));
        collection.updateOne(filter, document);
        document = new Document("$set", new Document("usersnickname", usersNickname));
        collection.updateOne(filter, document);
    }

    public static void removeGroup(String group_id,String [] user_ids){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.eq("group_id",group_id);
        FindIterable findIterable = collection.find(filter);
        Document rDocument= new Document();
        MongoCursor cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            rDocument=(Document) cursor.next();
        }
        List<String> users=( List<String>)rDocument.get("users");
        List<String> usersNickname=( List<String>)rDocument.get("usersnickname");
        //List<String>  rUsers=users;
        for(int i=0;i<user_ids.length;i++){
            int iCount=-1;
            for(int j=0;j< users.size();j++){
                if(user_ids[i].equals(users.get(j))){
                    iCount=j;
                }
            }
            if(iCount>=0){
                users.remove(iCount);
                usersNickname.remove(iCount);
            }

        }
        Document document = new Document("$set", new Document("users", users));
        collection.updateOne(filter, document);
        document = new Document("$set", new Document("usersnickname", usersNickname));
        collection.updateOne(filter, document);

    }



    public static void exitGroup(String group_id,String user_id){
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.eq("group_id",group_id);
        FindIterable findIterable = collection.find(filter);
        Document rDocument= new Document();
        MongoCursor cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            rDocument=(Document) cursor.next();
        }
        List<String> users=( List<String>)rDocument.get("users");
        List<String> usersNickname=( List<String>)rDocument.get("usersnickname");
        int iCount=-1;
        for(int j=0;j< users.size();j++){
                if(user_id.equals(users.get(j))){
                    iCount=j;
                }
        }
        if(iCount>=0){
            users.remove(iCount);
            usersNickname.remove(iCount);
        }
        Document document = new Document("$set", new Document("users", users));
        collection.updateOne(filter, document);
        document = new Document("$set", new Document("usersnickname", usersNickname));
        collection.updateOne(filter, document);

    }

    public static void updateGroupUserNickName(String group_id,String user_id,String nickName){

        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.eq("group_id",group_id);
        FindIterable findIterable = collection.find(filter);
        Document rDocument= new Document();
        MongoCursor cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            rDocument=(Document) cursor.next();
        }
        List<String> users=( List<String>)rDocument.get("users");
        List<String> usersNickname=( List<String>)rDocument.get("usersnickname");

        int iCount=-1;
        for(int j=0;j< users.size();j++){
            if(user_id.equals(users.get(j))){
                iCount=j;
            }
        }
        //System.out.println(".........."+iCount);
        if(iCount>=0){
            usersNickname.set(iCount,nickName);
        }
        Document document = new Document("$set", new Document("usersnickname", usersNickname));
        collection.updateOne(filter, document);
    }



    public static int  deleteGroup(String group_id,String admin_id){

        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.eq("group_id",group_id);
        FindIterable findIterable = collection.find(filter);
        MongoCursor cursor = findIterable.iterator();
        Document rDocument= new Document();
        while (cursor.hasNext()) {
            rDocument=(Document) cursor.next();
        }
        if(rDocument.get("adminid").equals(admin_id))
        {
            collection.deleteOne(filter);
            return 1;
        }else{
            return 2;
        }
        // 是admin才可以删除


        //System.out.println("更新公告成功");
    }

    public static List<Document> findUserGroup(String user_id){
        List<Document> documents = new ArrayList<Document>();
        MongoDatabase mongoDatabase = MongoUtil.getConnect();
        MongoCollection<Document> collection = mongoDatabase.getCollection("dygroup");
        Bson filter = Filters.in("users",user_id);
        FindIterable findIterable = collection.find(filter);
        MongoCursor cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            documents.add((Document) cursor.next());
        }
        return documents;
    }

}
