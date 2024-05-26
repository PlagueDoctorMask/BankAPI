package ru.gb.ServiceAPI.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.ServiceAPI.domain.Client;
import ru.gb.ServiceAPI.repository.APIRepository;
import ru.gb.ServiceAPI.service.APIService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/service")
public class APIController {

    private final APIService apiService;


    @PostMapping("/start")
    public void addPercents(){
        apiService.start();
    }


    @GetMapping("/all")
    public List<Client> showAll(){
        return apiService.getAllClients();
    }

    @PostMapping("/new")
    public Client newClient(@RequestBody Client client){
        return apiService.createClient(client);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable Long id){
        apiService.deleteById(id);
    }

    @GetMapping("/filter/phone/{phoneNumber}")
    public Client filterByPhoneNumber(@PathVariable String phoneNumber){
        return apiService.getByPhoneNumber(phoneNumber);
    }

    @GetMapping("/filter/age/{birthdate}")
    public List<Client> filterByAge(@PathVariable String birthdate){
        return apiService.filterUsersByAge(birthdate);
    }

    @GetMapping("/filter/email/{email}")
    public Client filterByEmail(@PathVariable String email){
        return apiService.getByEmail(email);
    }

    @GetMapping("/filter/name")
    public Client filterByName(@RequestParam String name){
        return apiService.getByName(name);
    }



}
