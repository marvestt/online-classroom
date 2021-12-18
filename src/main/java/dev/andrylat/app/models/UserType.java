package dev.andrylat.app.models;

public enum UserType {
    STUDENT("Student"),
    TEACHER("Teacher");
    
    private String name;
    
    UserType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
        
    }
    
    
}
