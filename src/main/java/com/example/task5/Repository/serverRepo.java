package com.example.task5.Repository;

import com.example.task5.model.Server;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface serverRepo extends MongoRepository<Server,String> {
    public List<Server> findByName(String name);
}
