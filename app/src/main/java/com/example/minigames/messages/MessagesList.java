package com.example.minigames.messages;

public class MessagesList {

    private String name, email, LastMessage, profilePic, chatKey;

    private int unseenMessages;

    public MessagesList(String name, String email, String lastMessage, String profilePic, int unseenMessages, String chatKey) {
        this.name = name;
        this.email = email;
        LastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }
}
