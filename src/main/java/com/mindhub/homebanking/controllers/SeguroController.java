package com.mindhub.homebanking.controllers;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mindhub.homebanking.dtos.*;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static utils.SendEmail.sendSimpleMessage;

@RestController
@RequestMapping("/api")
public class SeguroController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SeguroRepository seguroRepository;

    @Autowired
    private ClientSeguroRepository clientSeguroRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/seguros")
    public List<SeguroDTO> getSeguros(){
        return seguroRepository.findAll().stream().map(SeguroDTO::new).collect(toList());
    }
    @Transactional
    @PostMapping("/seguros")
    public ResponseEntity<Object> createSeguro(@RequestBody SeguroApplicationDTO solicitaSeguro, Authentication authentication){

        Optional<Seguro> seguroSolicitado =  seguroRepository.findById(solicitaSeguro.getSeguroId());
        Client client = this.clientRepository.findByEmail(authentication.getName());
        Account account = this.accountRepository.findByNumber(solicitaSeguro.getToAccountNumber());

        /*Validaciones*/
        if(!seguroSolicitado.isPresent()){
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }

        if(seguroSolicitado.get().getValor() > account.getBalance()){
            return new ResponseEntity<>("Saldo insuficiente para optar a este seguro", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de destino exista
        if(account.getClient() == null){
            return new ResponseEntity<>("La cuenta de destino no existe: ", HttpStatus.FORBIDDEN);
        }

        ClientSeguro clientSeguro = new ClientSeguro(seguroSolicitado.get().getValor() , client, seguroSolicitado.get());
        clientSeguroRepository.save(clientSeguro);

        Transaction transactionCredito = new Transaction(TransactionType.DEBIT,seguroSolicitado.get().getValor(),  " seguro aprobado", LocalDateTime.now(), account);
        transactionRepository.save(transactionCredito);

        account.setBalance(account.getBalance() - seguroSolicitado.get().getValor());
        accountRepository.save(account);


        try {
            JsonNode jsonNode = sendSimpleMessage("Debuwin support dionisio.santis@gmail.com",client.getEmail(),"Contratacion de "+ seguroSolicitado.get().getName(),"Estimado "+ client.getFirstName()+" "+client.getLastName()+" Te informamos que has contratado un "+ seguroSolicitado.get().getName()+ ", que sera cargado a la cuenta "+ account.getNumber());
            System.out.println(jsonNode.toString());
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Seguro creado con éxito", HttpStatus.CREATED);
    }
}
