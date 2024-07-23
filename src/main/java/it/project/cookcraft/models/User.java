package it.project.cookcraft.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User{
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String address;
    private String phoneNumber;
    private String password;
    private UserType userType;
}
