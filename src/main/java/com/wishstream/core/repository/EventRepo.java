package com.wishstream.core.repository;

import com.wishstream.core.model.Event;
import com.wishstream.core.model.User;
import com.wishstream.core.model.UserRelation;
import jakarta.validation.constraints.Email;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface EventRepo extends CrudRepository<Event,String> {

    Optional<Event> findAllByEventAndUserRelationId(String eventId, String userRelationId);

//    Optional<List<Event>> findAllByEventDateUtc(LocalDateTime eventDateUtc);
}
