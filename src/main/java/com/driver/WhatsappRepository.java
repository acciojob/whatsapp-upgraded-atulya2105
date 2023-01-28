package com.driver;

import org.springframework.stereotype.Repository;
import org.w3c.dom.ls.LSException;

import java.util.*;

@Repository
public class WhatsappRepository {

    Map<String,String> userDb = new LinkedHashMap<>();

    List<Group> groupList = new ArrayList<>();
    Map<String,Group> groupDb = new LinkedHashMap<>();
    HashMap<Integer,Message> msgDb = new LinkedHashMap<>();
    public void createUser (String name, String mobile) throws Exception{
        if(userDb.containsKey(mobile)){
            throw new Exception("User already exists");
        }
        userDb.put(mobile,name);
    }
    public Group createGroup(List<User>userList){
        int size = userList.size();
        if(size<2){
            return null;
        }
        else if(size==2){
            Group group = new Group();
            User user = userList.get(0);
            group.setAdmin(user);
            group.setName(userList.get(1).getName());
            group.setNumberOfParticipants(2);
            groupDb.put(userList.get(1).getName(),group);
            return group;
        }else {
            Group group = new Group();
            group.setAdmin(userList.get(0));
            String nameOfGroup = "Group "+ groupList.size()+1;
            group.setName(nameOfGroup);
            groupList.add(group);
            group.setNumberOfParticipants(size);
            groupDb.put(nameOfGroup,group);
            return group;
        }

    }

    public int createMessage(String content){
        Message msg = new Message();
        msg.setContent(content);
        Date date = new Date();
        //System.
        msg.setTimestamp(date);
        int size = msgDb.size();
        msg.setId(size+1);
        msgDb.put(size+1,msg);

        return size+1;

    }

    public int sendMessage(Message message, User user, Group group) throws Exception{
        List<User> users = group.getUserList();
        if(!users.contains(user)){
            throw new Exception("You are not allowed to send message");
        }
        if(!groupDb.containsKey(group)){
            throw  new Exception("Group does not exist");

        }

        group.getMessageList().add(message);
        List<Message>msg = user.getMessageList();
        msg.add(message);
        user.setMessageList(msg);
        //String name = userDb.getOrDefault();
        return group.getMessageList().size();
    }

    public void changeAdmin(User approver,User user,Group group) throws Exception{
        if(!groupDb.containsKey(group)){
            throw  new Exception("Group does not exist");

        }
        List<User> users = group.getUserList();
        if(!users.contains(approver)){
            throw new Exception("Approver does not have rights");
        }
        if(!users.contains(user)){
            throw new Exception("User is not a participant");
        }
//        User user1 = group.getAdmin();
        group.setAdmin(user);
        groupDb.put(group.getName(),group);
    }

    public int removeUser(User user) throws Exception{
        for(String group : groupDb.keySet()){
            List<User> userList = groupDb.get(group).getUserList();
            User admin = groupDb.get(group).getAdmin();
            for(User user1 :userList){
                if(user1.equals(admin) && user1.equals(user)){
                    throw new Exception("Cannot remove admin");
                }else if(user1.equals(user)){
                    break;
                }
            }
            Group group1 = groupDb.get(group);
            group1.getUserList().remove(user);
//            user.getGroup();
            return 0;
        }
        throw new Exception("User not found");
    }
}
