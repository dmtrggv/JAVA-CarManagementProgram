package use;

public class RegistrationNumber {

    private String number;

    public RegistrationNumber(String number) {
        this.number = number.toUpperCase();
    }

    @Override
    public String toString() {
        return this.number;
    }

}
