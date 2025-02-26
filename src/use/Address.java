package use;

/*
 * The Address class represents a physical address with attributes such as street type, street name, street number, town, and country.
 * It provides constructors to initialize the address and methods to get and set these attributes.
 */

public class Address {

    private String streetType;
    private String streetName;
    private String streetNumber;
    private String town;
    private String country;

    /*
     * Create Address object with all attributes
     */
    public Address(String streetType, String streetName, String streetNumber, String town, String country) {
        this.streetType = streetType;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.town = town;
        this.country = country;
    }

    /*
     * Create Address object from String
     */
    public Address(String input) {
        String[] parts = input.split(", ");
        String streetInfo = parts[0].trim();
        String[] streetParts = streetInfo.split(" ", 2);

        this.streetType = streetParts[0];
        String[] nameAndNumber = streetParts[1].split(" ", 2);
        this.streetName = nameAndNumber[0];
        this.streetNumber = (nameAndNumber.length > 1) ? nameAndNumber[1] : "";

        this.town = (parts.length > 1) ? parts[1] : "";
        this.country = (parts.length > 2) ? parts[2] : "";
    }

    //region Getters

    public String getStreetType() {
        return streetType;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getTown() {
        return town;
    }

    public String getCountry() {
        return country;
    }

    //endregion

    //region Setters

    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    //endregion

    @Override
    public String toString() {
        return this.streetType + " " + this.streetName + " " + this.streetNumber + ", " + this.town + ", " + this.country;
    }
}