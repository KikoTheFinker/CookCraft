package it.project.cookcraft.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    private Long id;
    private byte[] cv;
    private String motivationalLetter;
    private Long userId;
}
