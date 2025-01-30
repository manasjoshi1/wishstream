package com.wishstream.core.repository;

import com.wishstream.core.model.UserRelation;
import com.wishstream.core.model.UserRelation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRelationRepo  extends MongoRepository<UserRelation,String> {
    List<UserRelation> findEventsBetweenDates(LocalDate startDate, LocalDate endDate);

}
