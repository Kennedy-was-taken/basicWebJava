package com.myProgress.basicWeb.Services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myProgress.basicWeb.Entities.Db2.UserEntity;
import com.myProgress.basicWeb.Repository.Db2.AccountRepository;
import com.myProgress.basicWeb.Repository.Db2.AccountRoleRepository;
import com.myProgress.basicWeb.Repository.Db2.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private AccountRoleRepository _accountRoleRepository;

    @Autowired
    private AccountRepository _accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        UserEntity user = _userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("username or password is incorrect"));
        
        long accountId = _accountRepository.getIdByRefId(user.getId()).get();
        
        ArrayList<SimpleGrantedAuthority> role = new ArrayList<SimpleGrantedAuthority>();
        role.add(new SimpleGrantedAuthority("ROLE_" + _accountRoleRepository.getRoleNameByRefId(accountId).get()));

        return new User(user.getUsername(), user.getPassword(), role);
    }
    
}
