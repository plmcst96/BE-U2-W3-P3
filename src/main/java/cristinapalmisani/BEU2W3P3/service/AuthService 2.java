package cristinapalmisani.BEU2W3P3.service;

import cristinapalmisani.BEU2W3P3.entities.Role;
import cristinapalmisani.BEU2W3P3.entities.User;
import cristinapalmisani.BEU2W3P3.exception.BadRequestException;
import cristinapalmisani.BEU2W3P3.exception.UnauthorizedException;
import cristinapalmisani.BEU2W3P3.payload.user.UserDTO;
import cristinapalmisani.BEU2W3P3.payload.user.UserLoginDTO;
import cristinapalmisani.BEU2W3P3.repositories.UserDAO;
import cristinapalmisani.BEU2W3P3.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUser(UserLoginDTO body) {
        User user = userService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("wrong password");
        }
    }

    public User registerUser(UserDTO body) throws IOException {
        userDAO.findByEmail(body.email()).ifPresent(a -> {
            throw new BadRequestException("User with email " + a.getEmail() + " already exists");
        });
        User user = new User();
        user.setPassword(bcrypt.encode(body.password()));
        user.setName(body.name());
        user.setSurname(body.surname());
        user.setEmail(body.email());
        user.setRole(Role.USER);
        return userDAO.save(user);
    }
}
