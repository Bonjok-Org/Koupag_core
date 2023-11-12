package com.Koupag.services.services_implementations;

import com.Koupag.models.User;
import com.Koupag.repositories.UserRepository;
import com.Koupag.services.CitiesServices;
import com.Koupag.services.UserService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserServicesImpl implements UserService {

    private final UserRepository userRepository;
    private final CitiesServices citiesServices;
    private final LoadingCache<String, User> cache;
    
    @Autowired
    public UserServicesImpl(UserRepository userRepository, CitiesServices citiesServices) {
        this.userRepository = userRepository;
        this.citiesServices = citiesServices;
        
        cache = CacheBuilder.newBuilder()
                        .expireAfterAccess(20, TimeUnit.MINUTES)
                        .maximumSize(1000)
                        .build(new CacheLoader<String, User>() {
                            @Override
                            public User load(String email) throws Exception
                            {
                                return getUserByEmail(email);
                            }
                        });
    }

    @Override
    public User creteNewUser(User user) {
        userRepository.save(user);
        return user;
    }

   /* @Override
    public Optional<User> getUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }*/
    
    @Override
    public Optional<User> getUserByCNIC(String cnic) {
        return Optional.ofNullable(userRepository.findByCNIC(cnic));
    }
    
//    @Override
//    public Boolean existsByUsername(String username) {
//        return userRepository.existsByUsername(username);
//    }
//
    @Override
    public void cacheNewUser(String email, User user) {
        cache.put(email, user);
    }
    
    @Override
    public User getCachedUser(String email) {
        return cache.getIfPresent(email);
    }
    
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    
    
    
}
