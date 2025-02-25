package components;

import use.Address;

public class User {

    private String username;
    private String password;
    private String nameFirst;
    private String nameLast;
    private Address address;
    private String info;
    private int id;

    public User(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nameFirst = user.getNameFirst();
        this.nameLast = user.getNameLast();
        this.address = user.getAddress();
        this.info = user.getInfo();
        this.id = user.getID();
    }

    public User(String username, String password, String nameFirst, String nameLast, Address address, String info) {
        this.username = username;
        this.password = password;
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.address = address;
        this.info = info;
    }

    public User(int id, String username, String password, String nameFirst, String nameLast, Address address, String info) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.address = address;
        this.info = info;
    }

    //region Getters

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public String getNameFull() {
        return nameFirst + " " + nameLast;
    }

    public Address getAddress() {
        return address;
    }

    public String getInfo() {
        return info;
    }

    public int getID() {
        return id;
    }

    //endregion

    //region Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public void setNameFull(String nameFirst, String nameLast) {
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setID(int id) {
        this.id = id;
    }

    //endregion

    @Override
    public String toString() {
        return (
            "user { " +
            "db.id: " + this.id + ", " +
            "username: " + this.username + ", " +
            "password: " + this.password + ", " +
            "firstName: " + this.nameFirst + ", " +
            "lastName: " + this.nameLast + ", " +
            "address: " + this.address.toString() + " }"
        );
    }

}
