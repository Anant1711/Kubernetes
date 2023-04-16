package com.example.task5.controller;

import com.example.task5.Repository.serverRepo;
import com.example.task5.model.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class ServerController {
    @Autowired
    private serverRepo ServerRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAll(){
        List<Server> Allserver = ServerRepository.findAll();
        if(Allserver.size() > 0){
            return new ResponseEntity<List<Server>>(Allserver, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("404",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createServer(@RequestBody Server server){
        try{
            ServerRepository.save(server);
            return new ResponseEntity<Server>(server,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getbyID(@PathVariable("id") String id){
        Optional<Server> optionalSever = ServerRepository.findById(id);
        if(optionalSever.isPresent()){
            return  new ResponseEntity<>(optionalSever.get(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>("404",HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deletebyID(@PathVariable("id") String id){
        Optional<Server> optionalSeverfordelete = ServerRepository.findById(id);
        if(optionalSeverfordelete.isPresent()){
            ServerRepository.deleteById(id);
            return  new ResponseEntity<>("Deleted", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("404",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getbyName(@PathVariable("name") String name){
        List<Server> s = ServerRepository.findByName(name);
        if(s.size() > 0){
            return new ResponseEntity<>(s,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("404",HttpStatus.NOT_FOUND);
        }

    }

}
