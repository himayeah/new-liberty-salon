package com.bit.backend.entities;

import jakarta.persistence.*;

    @Entity
    @Table(name ="login_form")
    public class LoginFormEntity{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name ="first_name")
        private String firstName;

        @Column(name ="last_name")
        private String lastName;

        @Column(name ="email")
        private String email;

        @Column(name ="phone_number")
        private String phoneNumber;

    }


