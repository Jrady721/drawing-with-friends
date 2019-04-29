package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    // Declare User Table
    private IntegerProperty idx;
    private StringProperty id;
    private StringProperty password;

    // Constructor
    public User() {
        this.idx = new SimpleIntegerProperty();
        this.id = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
    }

    // user idx
    public int getUserIdx() {
        return idx.get();
    }

    public void setUserIdx(int userIdx) {
        this.idx.set(userIdx);
    }

    public IntegerProperty userIdxProperty() {
        return idx;
    }

    // user id
    public String getUserId() {
        return id.get();
    }

    public void setUserId(String userId) {
        this.id.set(userId);
    }

    public StringProperty userIdProperty() {
        return id;
    }

    // user password
    public String getUserPassword() {
        return password.get();
    }

    public void setUserPassword(String userPassword) {
        this.password.set(userPassword);
    }

    public StringProperty userPasswordProperty() {
        return password;
    }
}


