package com.wishstream.core.repository;

import com.wishstream.core.model.User;
import com.wishstream.core.model.UserRelation;
import jakarta.validation.constraints.Email;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepo extends MongoRepository<User,String> {

    Optional<User> findByEmailOrPhone(@Email(message = "Invalid email format") String email, String phone);
    @Query("{ 'userRelations': { $elemMatch: { 'user_relation_id': ?0 } } }")
    User findByUserRelationId(String userRelationId);
}
