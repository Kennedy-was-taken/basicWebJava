package com.myProgress.basicWeb.Services.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.myProgress.basicWeb.DTO.Employee.EmployeeDTO;
import com.myProgress.basicWeb.DTO.RefreshToken.RefreshTokenDTO;
import com.myProgress.basicWeb.DTO.User.UserPostDTO;
import com.myProgress.basicWeb.DTO.User.UserGetDTO;
import com.myProgress.basicWeb.Entities.Db2.RefreshTokenEntity;
import com.myProgress.basicWeb.Entities.Db2.UserEntity;
import com.myProgress.basicWeb.Model.ServiceResponseModel;
import com.myProgress.basicWeb.Repository.Db2.UserRepository;
import com.myProgress.basicWeb.Services.CustomUserDetailsService;
import com.myProgress.basicWeb.Services.Account.IAccountService;
import com.myProgress.basicWeb.Services.RefreshToken.IRefreshTokenService;
import com.myProgress.basicWeb.Services.Role.IAccountRoleService;
import com.myProgress.basicWeb.Services.Token.ITokenUtilService;
import com.myProgress.basicWeb.Services.UserProfile.IUserProfileService;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private IAccountService _accountService;

    @Autowired
    private IAccountRoleService _accountRoleService;

    @Autowired
    private IUserProfileService _userProfileService;

    @Autowired
    private IRefreshTokenService _refreshTokenService;

    @Autowired
    private ITokenUtilService _tokenUtilService;

    @Autowired
    private CustomUserDetailsService _userDetailsService;

    @Autowired
    private AuthenticationManager _authenticationManager;


    @Override
    public void saveNewUser(UserEntity newUser) {
        
        _userRepository.save(newUser);
    }

    @Override
    public String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public long getUserId(String username) {

        Optional<Long> id = _userRepository.getIdByUsername(username);

        return id.get();
    }

    @Override
    public void updateUsername(String username, long accountId) {
        
        Optional<UserEntity> userInfo = _userRepository.findById(_accountService.getUserRefId(accountId));

        userInfo.get().setUsername(username);

        saveNewUser(userInfo.get());
    }

    @Override
    public Boolean doesExist(String username) {
        
        return _userRepository.doesExist(username);
    }

    @Override
    public ServiceResponseModel<String> deleteUser(UserGetDTO deleteUser){
        ServiceResponseModel<String> serviceResponse = new ServiceResponseModel<String>();

        //checks to see if there is data
        if(deleteUser.getUsername().isEmpty()){
            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Cannot leave field empty");
            return serviceResponse;
        }

        //checks if the user exists
        if(!doesExist(deleteUser.getUsername())){
            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("The account by that username doesn't exist");
            return serviceResponse;
        }

        long accountId = _accountService.getAccountId(getUserId(deleteUser.getUsername()));
        _userProfileService.deleteProfile(accountId);
        _userRepository.deleteByUsername(deleteUser.getUsername());

        if(!doesExist(deleteUser.getUsername())){
            serviceResponse.setData("users gone");
            serviceResponse.setSuccessful(true);
            serviceResponse.setMessage("The account by that username no longer exists");
            return serviceResponse;
        }

        serviceResponse.setSuccessful(false);
        serviceResponse.setMessage("The account still exist");
        return serviceResponse;
    }

    @Override
    public ServiceResponseModel<UserGetDTO> createUser(UserPostDTO newUser) {
        
        ServiceResponseModel<UserGetDTO> serviceResponse = new ServiceResponseModel<UserGetDTO>();

        if(newUser.getUsername().isEmpty() || newUser.getPassword().isEmpty()){
            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Cannot leave field empty");
            return serviceResponse;
        }

        if(doesExist(newUser.getUsername())){
            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Username already exists");
            return serviceResponse;
        }

        //Create UserEntity object and save new user
        var user = createObject(newUser.getUsername(), newUser.getPassword());

        saveNewUser(user);

        //Creating the account
        long accountID = _accountService.CreateAccount(getUserId(user.getUsername()));
        
        //Creating the users role
        String role = _accountRoleService.CreateUserRole(accountID);

        //Creating and default user profile
        _userProfileService.createDefaultProfile(accountID, user.getUsername(), role);
        
        //sets the reponse data
        serviceResponse.setData(mapToUsernamePost(user)); //maps the data found in user and maps it to another object
        serviceResponse.setSuccessful(true);
        serviceResponse.setMessage("Account Created");

        return serviceResponse;

    }

    @Override
    public ServiceResponseModel<UserGetDTO> createEmployee(EmployeeDTO newEmployee) {
        
         ServiceResponseModel<UserGetDTO> serviceResponse = new ServiceResponseModel<UserGetDTO>();

        if(newEmployee.getUsername().isEmpty() || newEmployee.getPassword().isEmpty() || newEmployee.getRole().isEmpty()){
            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Cannot leave field empty");
            return serviceResponse;
        }

        if(doesExist(newEmployee.getUsername())){
            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Username already exists");
            return serviceResponse;
        }

        //Create new UserEntity object
        var user = createObject(newEmployee.getUsername(), newEmployee.getPassword());

        saveNewUser(user);

        //Creating the account
        long accountID = _accountService.CreateAccount(getUserId(newEmployee.getUsername()));
        
        // Creating the workers role
        String role = _accountRoleService.CreateWorkRole(accountID, newEmployee.getRole());

        //Creating and default user profile
        _userProfileService.createDefaultProfile(accountID, user.getUsername(), role);
        
        //sets the reponse data
        serviceResponse.setData(mapToUsernamePost(user)); //maps the data found in user and maps it to another object
        serviceResponse.setSuccessful(true);
        serviceResponse.setMessage("Account Created");

        return serviceResponse;
    }

    @Override
    public ServiceResponseModel<RefreshTokenDTO> Login(UserPostDTO returnedUser) throws Exception{

        ServiceResponseModel<RefreshTokenDTO> serviceResponse = new ServiceResponseModel<RefreshTokenDTO>();

        if(returnedUser.getUsername().isEmpty() || returnedUser.getPassword().isEmpty()){

            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Cannot leave field empty");
            return serviceResponse;
        }

        authenticate(returnedUser);

        final UserDetails userdetails = _userDetailsService.loadUserByUsername(returnedUser.getUsername());

        // creating RefreshTokenEntity object
         var token  = new RefreshTokenEntity();

        if(!_refreshTokenService.doesRefreshExist(_accountService.getAccountId(getUserId(userdetails.getUsername())))){

            // creating RefreshTokenEntity object
            var newToken  = new RefreshTokenEntity();
            newToken.setToken(_refreshTokenService.generateRefreshToken());
            newToken.setAccountRefId(_accountService.getAccountId(getUserId(userdetails.getUsername())));
            token = newToken;
        }
        else{

            // creating RefreshTokenEntity object
            var newToken = _refreshTokenService.getTokenObject(_accountService.getAccountId(getUserId(userdetails.getUsername())));
            newToken.get().setToken(_refreshTokenService.generateRefreshToken()); 
            token = newToken.get();     
        }
            
        _refreshTokenService.saveToken(token); // saves the token to the database

        // creating RefreshTokenPost object
        var returnToken = new RefreshTokenDTO();
        returnToken.setAccessToken(_tokenUtilService.generateToken(userdetails));
        returnToken.setRefreshToken(token.getToken());

        //sets the reponse data
        serviceResponse.setData(returnToken); //maps the data found in user and maps it to another object
        serviceResponse.setSuccessful(true);
        serviceResponse.setMessage("Successfully Logged in");
        
        return serviceResponse;

    }

    //Create UserEntity object
    private UserEntity createObject(String username, String password){

        var user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encryptPassword(password));

        return user;
    }

    private String encryptPassword(String password) {

        BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

        String encodedPassword = encode.encode(password);

        return encodedPassword;
    }

    private UserGetDTO mapToUsernamePost(UserEntity user) {

        UserGetDTO convert = new UserGetDTO();
        convert.setUsername(user.getUsername());
        return convert;
    }

    //authenticates the user
    private void authenticate(UserPostDTO returnedUser) throws Exception{
        try{
            _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(returnedUser.getUsername(), returnedUser.getPassword()));
        }
        catch(BadCredentialsException e){
            throw new Exception("INVALID", e);
        }
        catch(DisabledException e){
            throw new Exception("INVALID ACCOUNT", e);
        }
    }

   
    
}
