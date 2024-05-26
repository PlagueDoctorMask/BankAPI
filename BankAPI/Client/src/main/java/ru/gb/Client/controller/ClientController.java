package ru.gb.Client.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.Client.domain.Client;
import ru.gb.Client.dto.TransferRequest;
import ru.gb.Client.service.ClientService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/balance/{id}")
    public BigDecimal showBalance(@PathVariable Long id){
        return clientService.viewBalance(id);
    }

    @PutMapping("/edit/phone/{id}/{newPhoneNumber}/{password}")
    public void editPhoneNumber(@PathVariable Long id, @PathVariable String newPhoneNumber, @PathVariable String password){
        clientService.editPhoneNumber(id, newPhoneNumber,password);
    }

    @PutMapping("/edit/email/{id}/{newEmail}/{password}")
    public void editEmail(@PathVariable Long id, @PathVariable String newEmail, @PathVariable String password){
        clientService.editEmail(id, newEmail, password);
    }

    @PutMapping("/edit/extra/phone/{id}/{newPhoneNumber}/{password}")
    public void editExtraPhoneNumber(@PathVariable Long id, @PathVariable String newPhoneNumber, @PathVariable String password){
        clientService.editExtraPhoneNumber(id,newPhoneNumber,password);
    }

    @PutMapping("/edit/extra/email/{id}/{newEmail}/{password}")
    public void editExtraEmail(@PathVariable Long id, @PathVariable String newEmail, @PathVariable String password){
        clientService.editExtraEmail(id, newEmail, password);
    }

    @DeleteMapping("/delete/phone/{id}/{password}")
    public void deleteExtraPhoneNumber(@PathVariable Long id, @PathVariable String password){
        clientService.deleteExtraPhoneNumber(id, password);
    }

    @DeleteMapping("/delete/email/{id}/{password}")
    public void deleteExtraEmail(@PathVariable Long id, @PathVariable String password){
        clientService.deleteExtraEmail(id, password);
    }

    @PostMapping("/new/phone/{id}/{newPhoneNumber}/{password}")
    public void addExtraPhoneNumber(@PathVariable Long id, @PathVariable String newPhoneNumber, @PathVariable String password){
        clientService.addExtraPhoneNumber(id, newPhoneNumber, password);
    }

    @PostMapping("/new/email/{id}/{newEmail}/{password}")
    public void addExtraEmail(@PathVariable Long id, @PathVariable String newEmail, @PathVariable String password){
        clientService.addExtraEmail(id, newEmail, password);
    }

    @GetMapping("/filter/phone/{phoneNumber}")
    public Client filterByPhoneNumber(@PathVariable String phoneNumber){
        return clientService.getByPhoneNumber(phoneNumber);
    }

    @GetMapping("/filter/age/{birthdate}")
    public List<Client> filterByAge(@PathVariable String birthdate){
        return clientService.filterUsersByAge(birthdate);
    }

    @GetMapping("/filter/email/{email}")
    public Client filterByEmail(@PathVariable String email){
        return clientService.getByEmail(email);
    }

    @GetMapping("/filter/name")
    public Client filterByName(@RequestParam String name){
        return clientService.getByName(name);
    }

    @PostMapping("/transfer")
    public void transferMoney(@RequestBody TransferRequest request) {
        clientService.transferMoney(request.getSenderAccountID(), request.getReceiverAccountID(), request.getAmount());
    }

}
