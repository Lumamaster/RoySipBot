import java.io.Serializable;
import java.util.ArrayList;

public class Character implements Serializable {
    private String statusmessage;
    private String name;
    private String description;
    private String owner;

    public Character() { //constructor
        description = "";
        owner = "";
        statusmessage = "";
    }

    Character(String name) {
        this();
        statusmessage += "New character named " + name + " created!\n";
        this.name = name;
    }

    public String getStatus() {
        String temp = statusmessage;
        statusmessage = "";
        return temp;
    }

    void setOwner(String s) {
        owner = s;
        statusmessage += s + " is now the owner of " + getName() + ".\n";
    }

    String getOwner() {
        return owner;
    }

    void changeName(String n) { //changes name of character
        statusmessage += name + " is now named " + n + ".\n";
        name = n;
    }

    public String getName() {
        return name;
    }

    void setDescription(String s) { //changes description of character
        statusmessage += name + "'s description has been changed.\n";
        description = s;
    }

    String getDescription() {
        return description;
    }
}