package com.mindhub.homebanking.controllers;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static utils.SendEmail.sendSimpleMessage;

@RestController
@RequestMapping("api/")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        //Esto estaba antes:
        //public List<Account> getAccounts(){
        //return accountRepository.findAll();
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }


    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount (@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @PostMapping("/clients/current/accounts")
    private ResponseEntity<Object> createAccount(Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(client.getAccounts().size()>=3){
            return new ResponseEntity<>("Clients of accounts limit reached", HttpStatus.FORBIDDEN);

        }else{
            String accountNumber = ("VIN" + (int)(Math.random() * (10000000-1)+1));
            accountRepository.save(new Account(accountNumber, LocalDateTime.now(), 0 ,client));

            try {
                JsonNode jsonNode = sendSimpleMessage("Debuwin support dionisio.santis@gmail.com", client.getEmail(), "Creacion de cuenta", "Estimado " + client.getFirstName() + " " + client.getLastName()+" se ha registrado la cuenta numero " + accountNumber + " con exito");
            } catch (UnirestException e) {
                e.printStackTrace();
            }


            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

}

