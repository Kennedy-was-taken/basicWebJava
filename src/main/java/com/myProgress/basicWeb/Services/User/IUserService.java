package com.myProgress.basicWeb.Services.User;

import com.myProgress.basicWeb.DTO.Employee.EmployeeDTO;
import com.myProgress.basicWeb.DTO.RefreshToken.RefreshTokenDTO;
import com.myProgress.basicWeb.DTO.User.UserPostDTO;
import com.myProgress.basicWeb.DTO.User.UserGetDTO;
import com.myProgress.basicWeb.Entities.Db2.UserEntity;
import com.myProgress.basicWeb.Model.ServiceResponseModel;

public interface IUserService {
    
    void saveNewUser(UserEntity newUser);

    String getUsername();

    long getUserId(String username);

    void updateUsername(String username, long id);

    Boolean doesExist(String username);

    ServiceResponseModel<String> deleteUser(UserGetDTO deleteUser);
    
    ServiceResponseModel<UserGetDTO> createUser(UserPostDTO newUser);

    ServiceResponseModel<UserGetDTO> createEmployee(EmployeeDTO newEmployee);

    ServiceResponseModel<RefreshTokenDTO> Login(UserPostDTO returnedUser) throws Exception;
}
