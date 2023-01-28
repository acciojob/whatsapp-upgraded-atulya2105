package com.driver;

import org.springframework.stereotype.Repository;
import org.w3c.dom.ls.LSException;

import java.util.*;

@Repository
public class WhatsappRepository {

    Map<String,User> userDb = new LinkedHashMap<>();

    Map<Group, List<User>> groupOfUser = new HashMap<>();
    Map<Group,List<Message>> groupOfMessage = new HashMap<>();
    List<Message> messageList = new ArrayList<>();
    HashMap<User,List<Message>> listOfUserMessage = new HashMap<>();

    int groupCount = 0;
    int msgCount = 0;
    public void createUser (String name, String mobile) throws Exception{
        if(userDb.containsKey(mobile)){
            throw new Exception("User already exists");
        }
        User user = new User(name,mobile);


        userDb.put(mobile,user);
    }
    public Group createGroup(List<User>userList){
        int size = userList.size();
        if(size<2){
            return null;
        }
        else if(size==2){
            Group group = new Group();
           // User user = userList.get(0);
           // group.setAdmin(user);
            group.setName(userList.get(1).getName());
            group.setNumberOfParticipants(2);
            groupOfUser.put(group,userList);
            return group;
        }else {
            Group group=new Group("Group "+ ++groupCount,userList.size());
            groupOfUser.put(group,userList);

            return group;
        }

    }

    public int createMessage(String content){
        Message message=new Message(++msgCount,content);
        message.setTimestamp(new Date());
        messageList.add(message);
        return msgCount;

    }


    public int sendMessage(Message message, User user, Group group) throws Exception{
        if(!groupOfUser.containsKey(group)){
            throw  new Exception("Group does not exist");

        }
        //groupDb.containsKey(group.getName());
        List<User> users = groupOfUser.get(group);
        if(!users.contains(user)){
            throw new Exception("You are not allowed to send message");
        }


        if(groupOfMessage.containsKey(group)){

            groupOfMessage.get(group).add(message);
        }else{
            List<Message>msg = new ArrayList<>();
            msg.add(message);
            groupOfMessage.put(group,msg);
        }


        if(listOfUserMessage.containsKey(user))
        {
            listOfUserMessage.get(group).add(message);
        }
        else
        {
            List<Message> messages=new ArrayList<>();
            messages.add(message);
            listOfUserMessage.put(user,messages);
        }

        return listOfUserMessage.get(group).size();

    }

    public void changeAdmin(User approver ,User user,Group group) throws Exception{

        if(!groupOfUser.containsKey(group))
        {
            throw new Exception("Group does not exist");
        }

        User pastAdmin=groupOfUser.get(group).get(0);

        if(!approver.equals(pastAdmin))
        {
            throw new Exception("Approver does not have rights");
        }

        boolean check=false;
        for(User user1:groupOfUser.get(group))
        {
            if(user1.equals(user))   check=true;
        }

        if(!check)
        {
            throw new Exception("User is not a participant");
        }

        User newAdmin=null;

        Iterator<User> userIterator = groupOfUser.get(group).iterator();

        while(userIterator.hasNext())
        {
            User u= userIterator.next();
            if(u.equals(user))
            {
                newAdmin = u;
                userIterator.remove();
            }
        }

        groupOfUser.get(group).add(0,newAdmin);

    }

    public int removeUser(User user) throws Exception{
        boolean userFound = false;
        int groupSize = 0;
        int messageCount = 0;
        int overallMessageCount = messageList.size();
        Group groupToRemoveFrom = null;
        for (Map.Entry<Group, List<User>> entry : groupOfUser.entrySet()) {
            List<User> groupUsers = entry.getValue();
            if (groupUsers.contains(user))
            {
                userFound = true;
                groupToRemoveFrom = entry.getKey();
                if (groupUsers.get(0).equals(user))
                {
                    throw new Exception("Cannot remove admin");
                }
                groupUsers.remove(user);
                groupSize = groupUsers.size();
                break;
            }
        }
        if (!userFound)
        {
            throw new Exception("User not found");
        }

        if (groupOfMessage.containsKey(user))
        {
            messageCount = groupOfMessage.get(user).size() - 2;
            groupOfMessage.remove(user);
        }


        return groupSize + messageCount + overallMessageCount;
        }


}
