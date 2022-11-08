package com.example.Payments.services;

import com.example.Payments.dto.UserDTO;
import com.example.Payments.dto.UserInfo;
import com.example.Payments.models.Account;
import com.example.Payments.models.User;
import com.example.Payments.repositories.AccountsRepository;
import com.example.Payments.repositories.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService {

    private UsersRepository usersRepository;
    private ModelMapper modelMapper;
    private AccountService accountService;

    @Autowired
    public UserService(UsersRepository usersRepository, AccountsRepository accountsRepository, ModelMapper modelMapper, AccountService accountService) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.accountService = accountService;
    }

    public List<User> findAll() {
        return usersRepository.findAll();
    }

    public User findOne(int id) {
        return usersRepository.findById(id).orElse(null);
    }

    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserInfo convertToUserInfo(User user){
        UserInfo userInfo=new UserInfo();
        userInfo.setName(user.getName());
        userInfo.setAccountDTOSet(user.getAccounts()
                .stream().map(account->accountService.convertToDTO(account)).collect(Collectors.toSet()));
        return userInfo;
    }
}
