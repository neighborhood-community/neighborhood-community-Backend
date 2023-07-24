package project.neighborhoodcommunity.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.neighborhoodcommunity.dto.RequestSignUpDto;
import project.neighborhoodcommunity.entity.User;
import project.neighborhoodcommunity.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> join(RequestSignUpDto requestSignUpDto) {
        User user = buildUserEntity(requestSignUpDto);
        userRepository.save(user);
        return Optional.of(user);
    }

    private User buildUserEntity(RequestSignUpDto requestSignUpDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(requestSignUpDto, User.class);
    }
}
