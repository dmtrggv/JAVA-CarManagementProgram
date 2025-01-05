package components;

import use.RegistrationNumber;

public abstract class Vehicle {

    public enum vehicleType {
        CAR, MOTORCYCLE
    }

    private vehicleType type;
    private RegistrationNumber registrationNumber;
    private String brand;
    private String model;

    public Vehicle(vehicleType type, RegistrationNumber registrationNumber, String brand, String model) {
        this.type = type;
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
    }

    //region Getters

    public vehicleType getType() {
        return type;
    }

    public RegistrationNumber getRegistrationNumber() {
        return registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    //endregion

    //region Setters

    public void setType(vehicleType type) {
        this.type = type;
    }

    public void setRegistrationNumber(RegistrationNumber registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }


    //endregion
}