package components;

/*
 * The Garage class represents a garage with an ID and a name.
 * It provides getters and setters for both the ID and name attributes.
 */

public class Garage {

    private int id;
    private String name;

    public Garage(String name) {
        this.name = name;
    }

    public Garage(String name, int id) {
        this.id = id;
        this.name = name;
    }

    //region Getters

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    //endregion

    //region Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setID(int id) {
        this.id = id;
    }

    //endregion

}
