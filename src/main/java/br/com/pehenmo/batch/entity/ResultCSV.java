package br.com.pehenmo.batch.entity;

public class ResultCSV {

    private int id;
    private String firstName;
    private String lastName;
    private String identificador;

    public static String[] filds(){
        return new String[] {"id", "firstName", "lastName", "identificador"};
    }

    public ResultCSV(int id, String firstName, String lastName, String identificador) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identificador = identificador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
