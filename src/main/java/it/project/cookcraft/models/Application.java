package it.project.cookcraft.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "application")
@Data
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(name = "cv")
    private byte[] cv;

    @Column(name = "motivationalletter")
    private String motivationalLetter;

    @OneToOne(mappedBy = "user_id")
    User user;
}
