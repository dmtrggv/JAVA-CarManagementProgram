package use;

public class Address {

    private String streetType;
    private String streetName;
    private String streetNumber;
    private String town;
    private String country;

    public Address(String streetType, String streetName, String streetNumber, String town, String country) {

        this.streetType = streetType;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.town = town;
        this.country = country;

    }

    public Address(String input) {

        String[] parts = input.split(", ");
        String streetInfo = parts[0].trim();

        // Split the street info into the street type and the rest (street name + street number)
        String[] streetParts = streetInfo.split(" ", 2);

        this.streetType = streetParts[0];  // street type (e.g., "ул.")

        // If the second part of streetInfo exists, split it into street name and number
        if (streetParts.length > 1) {
            String[] nameAndNumber = streetParts[1].split("(?<=\\D)\\s+(?=\\d)", 2); // Split before the first number

            this.streetName = nameAndNumber[0].trim();  // street name (may contain multiple words)
            this.streetNumber = (nameAndNumber.length > 1) ? nameAndNumber[1].trim() : "";  // street number
        } else {
            this.streetName = "";
            this.streetNumber = "";
        }

        // If there are more parts (town, country), assign them accordingly
        this.town = (parts.length > 1) ? parts[1].trim() : "";
        this.country = (parts.length > 2) ? parts[2].trim() : "";

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