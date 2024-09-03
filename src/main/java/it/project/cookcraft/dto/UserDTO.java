package it.project.cookcraft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userName;
    private String userSurname;
    private String email;
    private String address;
    private String phoneNumber;
}
