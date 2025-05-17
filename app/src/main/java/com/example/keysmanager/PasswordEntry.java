package com.example.keysmanager;

public class PasswordEntry {
    private int id;
    private String title;
    private String username;
    private String encryptedPassword;
    private String notes;

    // Constructors, getters and setters
    public PasswordEntry() {}

    public PasswordEntry(String title, String username, String encryptedPassword, String notes) {
        this.title = title;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.notes = notes;
    }
    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEncryptedPassword() { return encryptedPassword; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return title + " (" + username + ")";
    }
}
