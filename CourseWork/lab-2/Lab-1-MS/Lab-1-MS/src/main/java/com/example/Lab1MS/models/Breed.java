package com.example.Lab1MS.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "breeds")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "deleted", allowGetters = true)
public class Breed {

    private @Id @GeneratedValue Long breed_id;
    @NotBlank
    private String Name;
    @NotBlank
    private String HairLong;
    @NotBlank
    private String Rareness;
    private boolean deleted = false;


    public Breed(String Name, String HairLong, String Rareness) {
        this.Name = Name;
        this.HairLong = HairLong;
        this.Rareness = Rareness;
    }

    public String getName() {
        return this.Name + " " + this.HairLong;
    }

    public void setName(String name) {
        String[] parts =name.split(" ");
        this.Name = parts[0];
        this.HairLong = parts[1];
    }

    public boolean getDeleted() {return deleted;}
    public void setDeleted(boolean deleted){this.deleted = deleted;}

    public Breed(){

    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

}
