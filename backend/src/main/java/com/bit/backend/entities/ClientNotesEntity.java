package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client_notes")
public class ClientNotesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "stylist_name")
    private String stylistName;

    @Column(name = "note_type")
    private String noteType;

    @Column(name = "note_content")
    private String noteContent;

    @Column(name = "note_date")
    private String noteDate;

}
