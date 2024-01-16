# Authentication and Authorization using Json Web Token (JWT)

A simple application for creating a json web token to authenticate and authorize a user, so that only authorized users can access and manipulate data


## Table of Content
- [Overview](#overview)
- [Technology](#technology)
- [Dependencies](#dependencies)
- [Usage](#usage)
<!-- [Refresh Token](#refresh-token) -->


## Overview

This project utilizes JSON Web Tokens (JWT) for secure authentication and authorization. 
In the context of this project, JWT is used to manage user authentication and control access to various resources.


### Authentication

Authentication is the process of verifying the identity of a user. In this project, 
the authentication flow involves the following steps:

1. **User Registration:**
   - Users can register by providing necessary information.
   - Upon successful registration, a unique JWT is generated and sent to the user.

2. **User Login:**
   - Registered users can log in by providing valid credentials.
   - Upon successful login, a new JWT and refresh token is generated. The Jwt token is issued, serving as a token of the user's authenticated session.

3. **Token Usage:**
   - The JWT is included in the headers of subsequent requests as the `Authorization` bearer token.
   - The server validates the token to authenticate the user and authorize access to protected resources.


### Authorization

Authorization determines what actions a user is allowed to perform after being authenticated. 
JWTs carry claims that define the user's scope. Authorization in this project follows these principles:

1. **Claims in JWT:**
   - The server checks these claims to ensure that the user has the necessary permissions for a given operation.


2. **Token Expiration:**
   - JWTs have a configurable expiration time, enhancing security by limiting the window of opportunity 
     for unauthorized access.

3. **Expired Token**
   - Once a token has expired, it becomes invalid and can be refreshed by passing the refresh token and expired token to generate a new token


### Multiple Database Configuration
1. The application uses two database schemas to store application data.
2. Type of databases : 
   - User Database
   - Account Database
3. User database holds the data of the users profile.
4. Account database holds the data of the users account information.

### Spring Security
1. Provides a robust authentication mechanisms.
2. Enables fine-grained authorization control, to define access rules based on roles and permissions to further secure the applications endpoint

## Technology
1. Visual Studio Code
2. Spring Boot 3.2.1-SNAPSHOT
3. Java
4. Postman

## Dependencies
1. Spring Web
2. Spring Boot Devtools
3. Spring Security
4. Spring Data JPA
5. MS SQL Server Driver
6. Lombok
7. jjwt-api : version 0.12.3
8. jjwt-impl : version 0.12.3
9. jjwt-jackson : version 0.12.3

## Usage

### Notice
   - Please be aware that the application does not automatically create or generate schemas for database interaction. 

   - To address this, navigate to the folder`./basicWeb_Scripts` and employ the provided scripts to generate the required schemas. Additionally, in the file `./src/main/resources/application.properties,` update the `connectionString` to reflect your specific connection string. 

   - Ensure that any other properties in the file are adjusted as needed to facilitate smooth interaction between the application and the database.

   - If the access token has expired during your testing, skip straight to the [#Refresh Token](#refresh-token) section and follow the steps to refresh your access token. The intentional quick expiration, set to five minutes, is for testing purposes.

### User Account Creation

   - Start the application and open Postman and click on the **New** button to create a new request. In the new request tab, Select **Get** as the request type. Pasted the URL generated from the application int the url field in Postman. 

   - At the end of the url insert `api/auth/signup`. Select the body tab and select row, ensure the type selected is json.
   
   - In the body, create two json parameters `username` and `password`. You can than fill in the blanks spaces next to the parameters with data to create an account. 
   
   - Upon successful submission, a notification will inform you that the user account has been successfully created.

   - Adding user credential

   ![Creation of user account](/images/createUser.PNG) 

   - Account Created

   ![User account created successfully](/images/successfullyUserCreated.PNG)
   

### User Account Accessibility

   #### Token generation

   - Once you have successfully created the user account, Create a new post request tab and proceed to passed at the end of the url `/api/auth/login`. 
   
   - Using the same steps in the previous **Get** request tab , create the same json parameters and enter the login credentials for the user. 
   
   - The application will authenticate the credentials against those stored in the database. If the credentials are found and match, a success message will be sent back, accompanied by the generated access and refresh tokens.

   - Successfully logged in

   ![User logged in successfully](/images/usersToken.PNG)

   #### User Profile

   - After successfully logging in and obtaining the access token, Create a new **Get** request tab and proceed to passed at the end of the URL `/api/account/profile`. 
   
   - In the **Authorization** tab, select **Bearer Token** from the dropdown menu next to **Type**. Paste the copied access token into the empty textbox next to **Token**.

   - Authorization

     ![Bearer Token](/images/bearerToken.PNG)
   
   - There is no need to create the body and just execute the request by clicking 'Send'. Upon receiving the response, you will observe that the profile data, with the exception that the `username` displayed will be that of the account instead of the default `user`

   - Default Profile

     ![Default profile](/images/UserProfileRetrieved.PNG)

   - To reflect a proper profile, create a new POST request tab in Postman. Follow the same steps used for creating the POST request to create an account. However, this time, use new parameters such as `firstName`, `middleName`, `lastName`, and `age`, and fill them out with relevant data for the user's profile. 
   
   - It's crucial to ensure that the access token is passed in the Authorization tab to authenticate and gain access to the API; otherwise, authentication will fail, and the API won't be accessible

   - Updating profile

     ![Updating Profile](/images/UserProfileUpdated.PNG)

   - Once updated, go back to the previous `Get` request and the profile will display the updated profile

   - Updated profile

     ![Updated Profile](/images/nejisProfileUpdated.PNG)

   #### Unauthorized accesss

   - Authorization is limited to specific endpoints, namely `signup`, `login`, `profile`, and `update`. Users do not have authorization to access any other endpoint outside of these specified ones. In the event that a user attempts to access an unmentioned endpoint.
   
   - For example, create a new **Get** request with the the endpoint `api/admin/allUserAccounts`, include the access token in the authorization tab, execute the request and the response will not include any data as shown below and the user will be seen as unauthorized.

   - Unauthorized

     ![Unauthorized](/images/unauthozied.PNG)

### Admin Account Accessability
   
   #### Create Admin Account

   - To create the admin account, go to the test folder and open the file `basicWebApplicationTests`.java. Run the test method named `createAdmin` to initiate the process of creating the admin account.

   - Admin Account method

     ![Admin account](/images/testMethod.PNG)

   #### Create Employee
   
   - Login with the admin credentials to obtain the access token and refresh token. As an admin, you have unrestricted access to all endpoints, including those that regular user accounts are not authorized to use. 
   
   - Create a new POST request with JSON parameters `username` and `password`, filling in the necessary data. Execute the request to successfully create the employee account.

   - New Employee Account

     ![Creating Employeee account](/images/employeeCreated.PNG)

   - For this program, the employee account has the same restricted access to specific endpoint thats the same as the user account. The only diffrence between the two account is the role accessed to each account. The employee account will have the role of employee.

   #### Role Change

   - Head back to the `allUserAccounts` request and replace the existing authorization Bearer token with the admin access token. Execute the request to see the data being returned, which should now display information of all the user accounts admin account, in contrast to the restricted data visible with a regular user account.

   - All User Accounts

     ![User Accounts](/images/getUsers.PNG)

   - With the list of user accounts being display, you probably notice the user account we created `neji`. we will change the role of neji's account from `USER` to `EMPLOYEE`.

   - Create a new POST request with the endpoint `/api/admin/changeRole`, create the JSON parameters `role` and `username`. Fill out the `role` and `username` fields with the desired data, then execute the request. A successful response will be returned, confirming the change in the account's role.

   - Role Change 

     ![Role changed](/images/roleChange.PNG)

   - Create a new get request with the endpoint `/api/admin/allWorkAccounts`. no need for a body and execute the request. In the response, a list of employees will be returned and you'll notice a new employee added **neji**.

   - New Employee Added

     ![New employee](/images/nejiNewEmployee.PNG)
   
   #### Delete account

   - Create a new POST request with the endpoint `/api/admin/deleteAccount`, this endpoint will be used to delete an account. Create json parameter username. Fill out the `username` field with the employee username, for example, `obito`, then execute the request. A successful response will be returned, confirming the deletion of the employee account.

   - Account Deleted

     ![Account deleted](/images/deleteUser.PNG)

   - if you go back to the `Get` request `/api/admin/allWorkAccounts` and execute the request. You will notice that the account `obito` no longer exists.

   - Account Removed

     ![Account removed](/images/getAllEmployee.PNG)

### Refresh Token

   #### Set Header

   - When denied access to the endpoints due to token expiration, initiate the token refresh process. Create a new POST request with the endpoint `/api/refresh`. In the Authorization tab, use the expired token. 
   
   - To complete the request, move to the `Header` tab and add a new header named `isRefreshToken` with the value set to `true`. This ensures a successful token refresh as shown below.

   - Header Set

     ![Header set](/images/header.PNG)

   #### Re-generating Tokens

   - Initiate the token refresh process by creating a new POST request with the endpoint `/api/refresh`. Add new parameters `accessToken` and `refreshToken` in the request, filling these fields with the `expired access` token and `refresh` token retrieved from the login endpoint. 
   
   - Execute the request, and upon success, the response will include the newly generated `access` token and `refresh` token. You can use the new token to regain access to the endpoints.

   - Generated Tokens
   
     ![Generated Tokens](/images/refreshToken.PNG)

