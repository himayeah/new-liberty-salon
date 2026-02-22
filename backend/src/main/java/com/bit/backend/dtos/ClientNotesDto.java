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
    private String clientName;
    private String stylistName;
    private String noteType;
    private String noteContent;
    private String noteDate;

    public String getFirstName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getClientName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
