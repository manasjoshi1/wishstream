package com.wishstream.core.repository;

import com.wishstream.core.model.User;
import com.wishstream.core.model.UserRelation;
import jakarta.validation.constraints.Email;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRelationRepo extends MongoRepository<UserRelation,String> {

    Optional<UserRelation> findUserRelationByUserRelationId(String userRelationId);

}
