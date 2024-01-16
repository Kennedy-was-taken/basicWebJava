package com.myProgress.basicWeb.Services.Account;

import java.util.ArrayList;

import com.myProgress.basicWeb.DTO.UserInfo.UserInfoDTO;
import com.myProgress.basicWeb.Entities.Db2.AccountEntity;
import com.myProgress.basicWeb.Model.ServiceResponseModel;

public interface IAccountService {
    
    long getAccountId(long userId);

    void saveNewAccount(AccountEntity newAccount);

    long getUserRefId(long accountId);

    long CreateAccount(long userId);

    ServiceResponseModel<ArrayList<UserInfoDTO>> getUserAccounts();

    ServiceResponseModel<ArrayList<UserInfoDTO>> getWorkAccounts();
}
