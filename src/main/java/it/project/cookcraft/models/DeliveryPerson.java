package it.project.cookcraft.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
//@PrimaryKeyJoinColumn(name = "user_id")
public class DeliveryPerson extends User{
    int distanceCovered;
    boolean active;
}
