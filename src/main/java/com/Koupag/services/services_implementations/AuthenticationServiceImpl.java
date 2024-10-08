package com.Koupag.services.services_implementations;

import com.Koupag.dtos.login.LoginDTO;
import com.Koupag.dtos.login.LoginResponseDTO;
import com.Koupag.dtos.login.UserDOS;
import com.Koupag.execptions.UserNotFoundException;
import com.Koupag.execptions.UserRoleNotFound;
import com.Koupag.models.Address;
import com.Koupag.models.Roles;
import com.Koupag.models.User;
import com.Koupag.repositories.AddressRepository;
import com.Koupag.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserService userService;
    private final RolesService rolesService;
    private final AuthenticationManager authenticationManager;
    private final MyTokenService myTokenService;
    private final UserTypeService userTypeService;

    public AuthenticationServiceImpl(UserService userService, RolesService rolesService, AuthenticationManager authenticationManager,
                                     MyTokenService myTokenService, UserTypeService userTypeService) {
        this.userService = userService;
        this.rolesService = rolesService;
        this.authenticationManager = authenticationManager;
        this.myTokenService = myTokenService;
        this.userTypeService = userTypeService;
    }



    @Override
    public User registerUser(User user) throws Exception {
            Set<Roles> roles = new HashSet<>();
            final Roles newUserRole;
            if(rolesService.getByName(user.getUserType()).isPresent()){
                newUserRole = rolesService.getByName(user.getUserType()).get();
            }else {
                throw new UserRoleNotFound("The Role : "+user.getUserType()+" is not Found");
            }

            roles.add(newUserRole);
            String userTypeRole = user.getUserType();
	    return userTypeService.createNewTypeUser(user,roles,userTypeRole);
    }

    @Override
    public Optional<LoginResponseDTO> loginUser(LoginDTO loginDTO) {
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getCnic(),loginDTO.getPassword())
            );
            String token = myTokenService.generateJwt(auth);

            // why this method gives a null userType => userService.getUserByCNIC(loginDTO.getCnic()).get()
            if (userService.getUserByCNIC(loginDTO.getCnic()).isPresent()){
                return Optional.of(new LoginResponseDTO(new UserDOS(userService.getUserByCNIC(loginDTO.getCnic()).get()), token));
            }else {
                throw new UserNotFoundException("the user with CNIC : "+ loginDTO.getCnic()+" : trying to login is not Found" );
            }

        } catch(AuthenticationException e){
            return Optional.empty();
        }
    }

    @Override
    public boolean checkUserRegistrationByEmail(String email) {
        return userService.existsByEmail(email);
    }


}
