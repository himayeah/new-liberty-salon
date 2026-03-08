package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientNotesDto {
    private Long id;
    private String clientName;
    private String stylistName;
    private String noteType;
    private String noteContent;
    private String noteDate;

    public String getFirstName() {
        return clientName;
    }

    public String getClientName() {
        return clientName;
    }
}
