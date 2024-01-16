package com.myProgress.basicWeb;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.myProgress.basicWeb.DTO.Employee.EmployeeDTO;
import com.myProgress.basicWeb.DTO.RefreshToken.RefreshTokenDTO;
import com.myProgress.basicWeb.DTO.User.UserPostDTO;
import com.myProgress.basicWeb.DTO.User.UserGetDTO;
import com.myProgress.basicWeb.Model.Roles;
import com.myProgress.basicWeb.Model.ServiceResponseModel;
import com.myProgress.basicWeb.Services.User.IUserService;

@SpringBootTest
class BasicWebApplicationTests {

	@Autowired
	private IUserService _userService;

	@Test
	void contextLoads() {
	}

	@Test
	void isFieldEmpty(){

		var newUser = new UserPostDTO();

		newUser.setPassword("");
		newUser.setUsername("");

		ServiceResponseModel<UserGetDTO> serviceResponse = _userService.createUser(newUser);

		assertTrue(serviceResponse.getData() == null);
		
	}

	@Test
	void isLoggingInSuccessful() throws Exception{
		
		var returnedUser = new UserPostDTO();
		
		returnedUser.setUsername("naruto");
		returnedUser.setPassword("naruto1");

		ServiceResponseModel<RefreshTokenDTO> serviceResponse = _userService.Login(returnedUser);

		assertTrue(serviceResponse.getData() != null);

	}

	@Test
	void CreateNewUserAccount(){
		
		var newUser = new UserPostDTO();
		
		newUser.setUsername("naruto");
		newUser.setPassword("naruto1");

		ServiceResponseModel<UserGetDTO> serviceResponse = _userService.createUser(newUser);

		assertTrue(serviceResponse.getData() != null);

	}

	@Test
	void failedToCreateNewUserAccount(){
		
		var newUser = new UserPostDTO();
		
		newUser.setUsername("naruto");
		newUser.setPassword("naruto1");

		ServiceResponseModel<UserGetDTO> serviceResponse = _userService.createUser(newUser);

		assertTrue(serviceResponse.getData() == null);
	}

	@Test
	void createAdmin(){
		
		var admin = new EmployeeDTO();
		admin.setUsername("admin");
		admin.setPassword("password");
		admin.setRole(Roles.ADMIN.toString());

		ServiceResponseModel<UserGetDTO> serviceResponse = _userService.createEmployee(admin);

		assertTrue(serviceResponse.isSuccessful());
		
	}

}
