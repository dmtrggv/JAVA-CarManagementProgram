package components;

import use.RegistrationNumber;

/*
 * The Vehicle class is an abstract class representing a vehicle with attributes such as type, registration number, brand, and model.
 * It provides constructors, getters, and setters to manage these attributes for different types of vehicles (e.g., car, motorcycle).
 */

public abstract class Vehicle {

    // Vehicle type
    public enum vehicleType {
        CAR, MOTORCYCLE
    }

    // Attributes
    private vehicleType type;
    private RegistrationNumber registrationNumber;
    private String brand;
    private String model;

    /*
     * Create vehicle object with all available attributes - this is only for the super() part
     */
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