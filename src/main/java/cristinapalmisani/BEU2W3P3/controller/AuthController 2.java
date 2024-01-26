package cristinapalmisani.BEU2W3P3.controller;

import cristinapalmisani.BEU2W3P3.entities.User;
import cristinapalmisani.BEU2W3P3.exception.BadRequestException;
import cristinapalmisani.BEU2W3P3.payload.user.UserDTO;
import cristinapalmisani.BEU2W3P3.payload.user.UserLoginDTO;
import cristinapalmisani.BEU2W3P3.payload.user.UserLoginResponseDTO;
import cristinapalmisani.BEU2W3P3.payload.user.UserResponseDTO;
import cristinapalmisani.BEU2W3P3.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO body) {
        String accessToken = authService.authenticateUser(body);
        return new UserLoginResponseDTO(accessToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@RequestBody @Validated UserDTO newUserPayload, BindingResult validation) throws IOException {
        if (validation.hasErrors()) {
            throw new BadRequestException("Ci sono errori nel payload!");
        }else {
            User newUser = authService.registerUser(newUserPayload);

            return new UserResponseDTO(newUser.getUuid());
        }
    }
}
