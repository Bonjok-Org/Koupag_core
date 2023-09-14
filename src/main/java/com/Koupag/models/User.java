package com.Koupag.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "User_Table")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long Id;
    private String name;
    @Column(unique = true)
    private String CNIC;
    @Column(unique = true)
    private String phoneNumber;
    @Column(unique = true)
    private String emailAddress;
    @Column(unique = true)
    private String username;
    private String password;
    @Transient
    private String userType;
    private LocalDate lastServed;

    @OneToOne(targetEntity = Address.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_address")
    private Address address;
    
    @ManyToMany(fetch =FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Roles> authorities;

    public User(String name, String phoneNumber, String emailAddress, String username, String password, Set<Roles> authorities) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
    public User(String name, String CNIC, String phoneNumber, String emailAddress, String username,
                String password, String userType, LocalDate lastServed, Address address, Set<Roles> authorities) {
        this.name = name;
        this.CNIC = CNIC;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.lastServed = lastServed;
        this.address = address;
        this.authorities = authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
