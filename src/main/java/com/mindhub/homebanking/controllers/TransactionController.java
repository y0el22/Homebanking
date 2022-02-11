package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static utils.SendEmail.sendSimpleMessage;

@RestController
@RequestMapping("api/")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    /*
    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransactions(Authentication authentication){
        return this.transactionRepository.findAll().stream().map(TransactionDTO::new).collect(toList());
    }

     */
    @RequestMapping("/transactons/{id}")
    public TransactionDTO getTransaction (@PathVariable Long id){
        return this.transactionRepository.findById(id).map(TransactionDTO::new).orElse(null);

    }

    @RequestMapping ("/transaction/export/{number}")
    public void exportToPdf (HttpServletResponse response, @PathVariable String number)throws DocumentException, IOException {
        response.setContentType("application/pdf");
        //Client client = this.clientRepository.findByEmail(authentication.getName());
        //String number="VIN001";
        Account account= accountRepository.findByNumber(number);

        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime= dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=transaction_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);

        List<TransactionDTO> listar= this.transactionRepository.findTransactionByAccount(account).stream().map(TransactionDTO::new).collect(Collectors.toList());
        PDFExportTransaction exporter= new PDFExportTransaction(listar);
        exporter.exportTransc(response);
    }

    @Transactional
    @PostMapping("/transactions")
    private ResponseEntity<Object> createTransaction(@RequestParam double amount, @RequestParam String description,
                                              @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                              Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());
        Account accountOrigen = this.accountRepository.findByNumber(fromAccountNumber);
        Account accountDestino = this.accountRepository.findByNumber(toAccountNumber);

        //Si se deja algun campo vacío
        if (amount == 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        //Que las cuentas no sea iguales
        if (fromAccountNumber== toAccountNumber) {
            return new ResponseEntity<>("Cuenta de destino y origen iguales", HttpStatus.FORBIDDEN);
        }
        //Que exista la cuenta de origen
        if(accountOrigen == null){
            return new ResponseEntity<>("Cuenta origen no exite", HttpStatus.FORBIDDEN);
        }
        //Que exista la cuenta de distino
        if(accountDestino == null){
            return new ResponseEntity<>("Cuenta destino no exite", HttpStatus.FORBIDDEN);
        }
        if(accountOrigen.getBalance() < amount){
            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
        }
        int i = 0;
        for(Account cuentas :client.getAccounts()){
            if(cuentas.getNumber() == accountOrigen.getNumber())
            {
                i=1;
            }
        }
        if (i == 0){
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebito = new Transaction(TransactionType.DEBIT, amount*-1, description, LocalDateTime.now(), accountOrigen);
        Transaction transactionCredito = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(), accountDestino);
        transactionRepository.save(transactionDebito);
        transactionRepository.save(transactionCredito);

        //Modificar saldo
        accountOrigen.setBalance(accountOrigen.getBalance() - amount);
        accountDestino.setBalance(accountDestino.getBalance() + amount);
        accountRepository.save(accountOrigen);
        accountRepository.save(accountDestino);

        try {
            JsonNode jsonNode = sendSimpleMessage("Debuwin support dionisio.santis@gmail.com", client.getEmail(), "Transaccion realizada " , "Estimado " + client.getFirstName() + ", Usted ha realizado una transferencia a la cuenta " + toAccountNumber + " desde su cuenta " + accountOrigen.getNumber() + " por un monto de $"+ amount);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Transacción realizada con éxito", HttpStatus.CREATED);
    }
}
