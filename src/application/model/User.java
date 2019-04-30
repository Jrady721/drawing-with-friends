//package application.model;
//
//import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//
//public class User {
//    // Declare User Table
//    private IntegerProperty idx;
//    private StringProperty id;
//    private StringProperty password;
//
//    // Constructor
//    public User() {
//        this.idx = new SimpleIntegerProperty();
//        this.id = new SimpleStringProperty();
//        this.password = new SimpleStringProperty();
//    }
//
//    // user idx
//    public int getUserIdx() {
//        return idx.get();
//    }
//
//    public void setUserIdx(int userIdx) {
//        this.idx.set(userIdx);
//    }
//
//    public IntegerProperty userIdxProperty() {
//        return idx;
//    }
//
//    // user id
//    public String getUserId() {
//        return id.get();
//    }
//
//    public void setUserId(String userId) {
//        this.id.set(userId);
//    }
//
//    public StringProperty userIdProperty() {
//        return id;
//    }
//
//    // user password
//    public String getUserPassword() {
//        return password.get();
//    }
//
//    public void setUserPassword(String userPassword) {
//        this.password.set(userPassword);
//    }
//
//    public StringProperty userPasswordProperty() {
//        return password;
//    }
//}
//
//

package application.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// 회원 모델 클래스
public class User {
    private final IntegerProperty id;
    private final StringProperty userName;
    private final StringProperty password;

    // 디폴트 생성자
    public User() {
        this(null, null, null);
    }

    public User(Integer id, String userName, String password) {
        this.id = new SimpleIntegerProperty();
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
    }

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }
}