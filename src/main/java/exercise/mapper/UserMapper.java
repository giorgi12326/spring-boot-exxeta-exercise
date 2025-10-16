package exercise.mapper;

import exercise.dto.RegisterUserDTO;
import exercise.dto.UserDTO;
import exercise.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(source = "username", target = "username")
//    @Mapping(source = "password", target = "password")
    User toEntity(UserDTO userDTO);

//    @Mapping(source = "username", target = "username")
//    @Mapping(source = "password", target = "password")
//    @Mapping(source = "role", target = "role")
    User toEntity(RegisterUserDTO userDTO);


//    @Mapping(source = "username", target = "username")
//    @Mapping(source = "password", target = "password")
//    @Mapping(source = "role", target = "role")
    RegisterUserDTO toDTO(User user);
}
