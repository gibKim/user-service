package com.example.userservice.controller;

import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import com.example.userservice.vo.UserDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class UserController {
    private Environment env;
    private UserService userService;

    private Greeting greeting;

    @GetMapping("/health_check")
    public String status(){
        return String.format("It's Working in User service on PORT %s"
                , env.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome(){
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getusers() {
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getusers(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);

        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

}
