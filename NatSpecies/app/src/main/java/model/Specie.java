package model;

public class Specie {

    private int id;
    private String commonName;
    private String latinName;


    public Specie(){

    }

    public Specie(int id, String commonName, String latinName) {
        this.id = id;
        this.commonName = commonName;
        this.latinName = latinName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    @Override
    public String toString() {
        return "Specie{" +
                "id=" + id +
                ", commonName='" + commonName + '\'' +
                ", latinName='" + latinName + '\'' +
                '}';
    }
}
