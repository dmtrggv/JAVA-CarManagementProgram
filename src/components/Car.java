package components;

import use.Date;
import use.RegistrationNumber;

/*
 * The Car class extends the Vehicle class and represents a car with attributes such as mileage, fuel type, horsepower, and production year.
 * It provides getters and setters for these attributes and a toString method to output car details in a readable format.
 * The class also includes various dates related to insurance, GTP, and wheel changes.
 */
public class Car extends Vehicle {

    private int     mileage;
    private int     lastOilChange;
    private double  mpg;
    private double  gasTank;
    private String  fuelType;
    private int     hp;
    private String  gearboxType;
    private int     yearProduction;
    private int     yearRegistration;
    private Date    dateInsurance;
    private Date    dateGTP;
    private Date    dateWheelChange;
    private String  garage;
    private String  info;

    /*
     * Create car only with Registration number
     */
    public Car(RegistrationNumber regNumber) {
        super(vehicleType.CAR, regNumber, null, null);
    }

    /*
     * Make car with all attributes
     */
    public Car(String brand, String model, RegistrationNumber registrationNumber, int mileage, int lastOilChange, double mpg, double gasTank, String fuelType, int hp, String gearboxType, int yearProduction, int yearRegistration, Date dateInsurance, Date dateGTP, Date dateWheelChange, String info, String garage) {
        super(vehicleType.CAR, registrationNumber, brand, model);
        this.mileage =          mileage;
        this.lastOilChange =    lastOilChange;
        this.mpg =              mpg;
        this.gasTank =          gasTank;
        this.fuelType =         fuelType;
        this.hp =               hp;
        this.gearboxType =      gearboxType;
        this.yearProduction =   yearProduction;
        this.yearRegistration = yearRegistration;
        this.dateInsurance =    dateInsurance;
        this.dateGTP =          dateGTP;
        this.dateWheelChange =  dateWheelChange;
        this.garage =           garage;
        this.info =             info;
    }

    //region Getters

    public int getMileage() {
        return mileage;
    }

    public int getLastOilChange() {
        return lastOilChange;
    }

    public double getMpg() {
        return mpg;
    }

    public double getGasTank() {
        return gasTank;
    }

    public String getFuelType() {
        return fuelType;
    }

    public int getHp() {
        return hp;
    }

    public String getGearboxType() {
        return gearboxType;
    }

    public int getYearProduction() {
        return yearProduction;
    }

    public int getYearRegistration() {
        return yearRegistration;
    }

    public Date getDateInsurance() {
        return dateInsurance;
    }

    public Date getDateGTP() {
        return dateGTP;
    }

    public Date getDateWheelChange() {
        return dateWheelChange;
    }

    public String getGarage() {
        return garage;
    }

    public String getInfo() {
        return info;
    }

    //endregion

    //region Setters

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setLastOilChange(int lastOilChange) {
        this.lastOilChange = lastOilChange;
    }

    public void setMpg(double mpg) {
        this.mpg = mpg;
    }

    public void setGasTank(double gasTank) {
        this.gasTank = gasTank;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setGearboxType(String gearboxType) {
        this.gearboxType = gearboxType;
    }

    public void setYearProduction(int yearProduction) {
        this.yearProduction = yearProduction;
    }

    public void setYearRegistration(int yearRegistration) {
        this.yearRegistration = yearRegistration;
    }

    public void setDateInsurance(Date dateInsurance) {
        this.dateInsurance = dateInsurance;
    }

    public void setDateGTP(Date dateGTP) {
        this.dateGTP = dateGTP;
    }

    public void setDateWheelChange(Date dateWheelChange) {
        this.dateWheelChange = dateWheelChange;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    //endregion

    @Override
    public String toString() {
        return "vehicle." + this.getType() + " { " +
               "regNum: " +         this.getRegistrationNumber() + ", " +
               "brand: " +          this.getBrand() + ", " +
               "model: " +          this.getModel() + ", " +
               "mileage: " +        this.getMileage() + ", " +
               "lastOilChange: " +  this.getLastOilChange() + ", " +
               "mpg: " +            this.getMpg() + ", " +
               "gasTank: " +        this.getGasTank() + ", " +
               "fuelType: " +       this.getFuelType() + ", " +
               "bhp: " +            this.getHp() + ", " +
               "gearbox: " +        this.getGearboxType() + ", " +
               "production: " +     this.getYearProduction() + ", " +
               "registration: " +   this.getYearRegistration() + ", " +
               "insurance: " +      this.getDateInsurance().toString(false) + ", " +
               "gtp: " +            this.getDateGTP().toString(false) + ", " +
               "wheelChange: " +    this.getDateWheelChange().toString(false) + ", " +
               "garage: " +         this.getGarage() + ", " +
               "info: " +           this.getInfo() +
               " }";
    }

}