package com.example.Payments.mapper;

import com.example.Payments.dto.UserDTO;
import com.example.Payments.models.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    private ModelMapper modelMapper;
    private AccountMapper accountMapper;

    @Autowired
    public UserMapper(AccountMapper accountMapper) {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(user -> user.skip(UserDTO::setAccountDTOSet))
                .setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(user -> user.skip(User::setAccounts))
                .setPostConverter(toEntityConverter());

        this.accountMapper = accountMapper;
    }

    private Converter<User, UserDTO> toDtoConverter() {
        return context -> {
            User source = context.getSource();
            UserDTO destination = context.getDestination();
            destination.setAccountDTOSet(source.getAccounts()
                    .stream()
                    .map(account -> accountMapper.toDto(account))
                    .collect(Collectors.toSet()));
            return context.getDestination();
        };
    }

    private Converter<UserDTO, User> toEntityConverter() {
        return context -> {
            UserDTO source = context.getSource();
            User destination = context.getDestination();
            destination.setAccounts(source.getAccountDTOSet()
                    .stream()
                    .map(account -> accountMapper.toEntity(account))
                    .collect(Collectors.toSet()));
            return context.getDestination();
        };
    }

    public UserDTO toDto(User entity) {
        return modelMapper.map(entity, UserDTO.class);
    }

    public User toEntity(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }
}
