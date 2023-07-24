package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.neighborhoodcommunity.dto.UserDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean join(UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail()))
            return false;
        User user = buildUserEntity(userDto);
        userRepository.save(user);
        return true;
    }

    private User buildUserEntity(UserDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDto, User.class);
    }
}
