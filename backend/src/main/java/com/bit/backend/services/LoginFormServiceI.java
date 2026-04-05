package com.bit.backend.services;


import com.bit.backend.dtos.LoginFormDto;

//write the method inside Interface class. And then Implement the method under "impl" sub-folder
public interface LoginFormServiceI {
    LoginFormDto addLoginFormEntity(LoginFormDto loginFormDto);
}
