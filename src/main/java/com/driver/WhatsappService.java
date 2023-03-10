package com.driver;

import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {

    WhatsappRepository whatsappRepository = new WhatsappRepository();

    public String createUser(String name,String mobileNo) throws Exception{

        return whatsappRepository.createUser(name,mobileNo);
    }

    public Group createGroup(List<User> users){
        Group group = whatsappRepository.createGroup(users);
        return group;
    }

    public int createMessage(String content ) {
        int id = whatsappRepository.createMessage(content);
        return id;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        int numberOfMessage = whatsappRepository.sendMessage(message,sender,group);
        return numberOfMessage;
    }

    public String changeAdmin(User approver, User admin,Group group) throws Exception{

        return whatsappRepository.changeAdmin(approver,admin,group);
    }

    public int removeUser(User user )throws  Exception{
        int number = whatsappRepository.removeUser(user);
        return number;
    }

    public String findMessage(Date start, Date end, int k){
        return "";
    }
}
