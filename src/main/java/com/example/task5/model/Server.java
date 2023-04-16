package com.example.task5.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@Document(collection = "server-detail")
public class Server {

    private String name;
    @Id
    private String id;
    private String language;
    private String framework;

}

