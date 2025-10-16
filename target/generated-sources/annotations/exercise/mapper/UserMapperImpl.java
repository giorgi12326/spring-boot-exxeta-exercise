package exercise.mapper;

import exercise.dto.RegisterUserDTO;
import exercise.dto.UserDTO;
import exercise.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-16T12:47:19+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( userDTO.getUsername() );
        user.setPassword( userDTO.getPassword() );

        return user;
    }

    @Override
    public User toEntity(RegisterUserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( userDTO.getUsername() );
        user.setPassword( userDTO.getPassword() );
        user.setRole( userDTO.getRole() );

        return user;
    }

    @Override
    public RegisterUserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        RegisterUserDTO registerUserDTO = new RegisterUserDTO();

        registerUserDTO.setUsername( user.getUsername() );
        registerUserDTO.setPassword( user.getPassword() );
        registerUserDTO.setRole( user.getRole() );

        return registerUserDTO;
    }
}
