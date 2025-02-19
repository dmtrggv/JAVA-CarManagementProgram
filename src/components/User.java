package components;

import use.Address;

public class User {

    private String username;
    private String password;
    private String nameFirst;
    private String nameLast;
    private Address address;
    private String info;
    private String dbPath;

    public User(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nameFirst = user.getNameFirst();
        this.nameLast = user.getNameLast();
        this.address = user.getAddress();
        this.info = user.getInfo();
    }

    public User(String username, String password, String nameFirst, String nameLast, Address address, String info) {
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

    public String getDbPath() {
        return dbPath;
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

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    //endregion

    @Override
    public String toString() {
        return (
            "user { " +
            "username: " + this.username + ", " +
            "password: " + this.password + ", " +
            "firstName: " + this.nameFirst + ", " +
            "lastName: " + this.nameLast + ", " +
            "address: " + this.address.toString() + " }"
        );
    }

}
