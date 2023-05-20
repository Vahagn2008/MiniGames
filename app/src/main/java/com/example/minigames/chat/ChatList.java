package com.example.minigames.chat;

public class ChatList {

    private String email, name, message, date, time;

    public ChatList(String email, String name, String message, String date, String time) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
