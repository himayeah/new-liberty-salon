package com.bit.backend.services;

import com.bit.backend.dtos.SignFormDto;

import java.util.List;

//why is every method written here under one interface? Himaya
public interface SignFormServiceI {
    SignFormDto addSignForm(SignFormDto signFormDto);
    List<SignFormDto> getData();
    SignFormDto updateSignForm(long id, SignFormDto signFormDto);

    SignFormDto deleteSignForm(long id);
}
