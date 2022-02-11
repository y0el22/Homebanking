package com.mindhub.homebanking.controllers;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static utils.SendEmail.sendSimpleMessage;

@RestController
@RequestMapping("api/")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/clients/currents/cards")
    public List<CardDTO> getCards(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().map(CardDTO::new).collect(toList());
    }

    @PostMapping("/clients/current/cards")
    private ResponseEntity<Object> createCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor,
                                              Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(client.getCards().stream().filter(card -> card.getType() == cardType).count() >=3){
            return new ResponseEntity<>("Ha superado el límite de tarjetas "+cardType +".", HttpStatus.FORBIDDEN);
        }
        String cardNumber = (int) ((Math.random() * (9999-1000))+1000) + "-" + (int) ((Math.random() * (9999-1000))+1000) + "-" + (int) ((Math.random() * (9999-1000))+1000) + "-" +(int) ((Math.random() * (9999-1000))+1000);
        int cvv = (int) ((Math.random() * (999-100))+100);
        Card card = new Card(client.getFirstName()+ " "+ client.getLastName(), cardType, cardColor, cardNumber, cvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client);
        cardRepository.save(card);
        try {
            JsonNode jsonNode = sendSimpleMessage("Debuwin support dionisio.santis@gmail.com", client.getEmail(), "Estimado " + client.getFirstName() + " " + client.getLastName(), "Usted ha realizado la creacion de una tarjeta de "+ card.getType() +" "+ card.getColor() +" numero "+ card.getNumber());
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
