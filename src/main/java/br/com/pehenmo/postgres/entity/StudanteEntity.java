package br.com.pehenmo.postgres.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "studant")
public class StudanteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String firstName;
    private String lastName;
    private String identificador;

    public StudanteEntity() {
    }

    public StudanteEntity(Integer id, String firstName, String lastName, String identificador) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identificador = identificador;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
