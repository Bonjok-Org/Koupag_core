package com.Koupag.mappers;

import com.Koupag.models.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class  UserMapper {

    private final PasswordEncoder passwordEncoder;


    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public Donor userToDonor(User user, Set<Roles> authorities){
        Donor donor = new Donor();
//        Location location = new Location();
        donor.setName(user.getName());
        donor.setAuthorities(authorities);
        donor.setEmail(user.getEmail());
        donor.setCNIC(user.getCNIC());
        donor.setUserType(user.getUserType());
        donor.setPhoneNumber(user.getPhoneNumber());
//        donor.setUsername(user.getUsername());
        donor.setPassword(passwordEncoder.encode(user.getPassword()));
        donor.setAddress(new Address(
                user.getAddress().getAreaName(),
                user.getAddress().getCity(),
                new Location(
                        user.getAddress().getLocation().getLatitude(),
                        user.getAddress().getLocation().getLongitude(),
                        donor.getAddress()
                ),
                donor
            )
        );
        return donor;
    }

    public Volunteer userToVolunteer(User user, Set<Roles> authorities){
        Volunteer volunteer = new Volunteer();
//        Location location = new Location();
        volunteer.setName(user.getName());
        volunteer.setAuthorities(authorities);
        volunteer.setEmail(user.getEmail());
        volunteer.setCNIC(user.getCNIC());
        volunteer.setUserType(user.getUserType());
        volunteer.setPhoneNumber(user.getPhoneNumber());
//        donor.setUsername(user.getUsername());
        volunteer.setPassword(passwordEncoder.encode(user.getPassword()));
        volunteer.setAddress(new Address(
                        user.getAddress().getAreaName(),
                        user.getAddress().getCity(),
                        new Location(
                                user.getAddress().getLocation().getLatitude(),
                                user.getAddress().getLocation().getLongitude(),
                                volunteer.getAddress()
                        ),
                        volunteer
                )
        );
        return volunteer;
    }

    public Recipient userToRecipient(User user, Set<Roles> authorities){
        Recipient recipient = new Recipient();
//        Location location = new Location();
        recipient.setName(user.getName());
        recipient.setAuthorities(authorities);
        recipient.setEmail(user.getEmail());
        recipient.setCNIC(user.getCNIC());
        recipient.setUserType(user.getUserType());
        recipient.setPhoneNumber(user.getPhoneNumber());
//        donor.setUsername(user.getUsername());
        recipient.setPassword(passwordEncoder.encode(user.getPassword()));
        recipient.setAddress(new Address(
                        user.getAddress().getAreaName(),
                        user.getAddress().getCity(),
                        new Location(
                                user.getAddress().getLocation().getLatitude(),
                                user.getAddress().getLocation().getLongitude(),
                                recipient.getAddress()
                        ),
                        recipient
                )
        );
        return recipient;
    }

    public User donorToUserModel(Donor donor, Set<Roles> authorities){
        User user = new User();
        user.setName(donor.getName());
        user.setAuthorities(authorities);
        user.setEmail(donor.getEmail());
        user.setCNIC(donor.getCNIC());
        user.setUserType(donor.getUserType());
        user.setPhoneNumber(donor.getPhoneNumber());
//        user.setUsername(donor.getUsername());
        user.setPassword(passwordEncoder.encode(donor.getPassword()));
        user.setAddress(donor.getAddress());
        return user;
    }
    public User volunteerToUserModel(Volunteer volunteer, Set<Roles> authorities){
        User user = new User();
        user.setName(volunteer.getName());
        user.setAuthorities(authorities);
        user.setEmail(volunteer.getEmail());
        user.setCNIC(volunteer.getCNIC());
        user.setUserType(volunteer.getUserType());
        user.setPhoneNumber(volunteer.getPhoneNumber());
//        user.setUsername(volunteer.getUsername());
        user.setPassword(passwordEncoder.encode(volunteer.getPassword()));
        user.setAddress(volunteer.getAddress());
        return user;
    }

    public User recipientToUserModel(Recipient recipient, Set<Roles> authorities){
        User user = new User();
        user.setName(recipient.getName());
        user.setAuthorities(authorities);
        user.setEmail(recipient.getEmail());
        user.setCNIC(recipient.getCNIC());
        user.setUserType(recipient.getUserType());
        user.setPhoneNumber(recipient.getPhoneNumber());
//        user.setUsername(recipient.getUsername());
        user.setPassword(passwordEncoder.encode(recipient.getPassword()));
        user.setAddress(recipient.getAddress());
        return user;
    }

}
