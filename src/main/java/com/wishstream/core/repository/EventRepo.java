package com.wishstream.core.repository;

import com.wishstream.core.model.User;
import com.wishstream.core.model.UserRelation;
import jakarta.validation.constraints.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends MongoRepository<User,String> {
    List<UserRelation> findEventsBetweenDates(LocalDate startDate, LocalDate endDate);

    Optional<User> findByEmailOrPhone(@Email(message = "Invalid email format") String email, String phone);
}
