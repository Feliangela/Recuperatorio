package com.elaparato.repository;

import com.elaparato.model.User;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface IUserRepository {

    List<User> findAll();
    List<User> findByUserName(String userName);
    Optional<User> findById(String id);
    User deleteUserById(String id);

}
