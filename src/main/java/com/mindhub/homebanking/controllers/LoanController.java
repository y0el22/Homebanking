package com.mindhub.homebanking.controllers;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static utils.SendEmail.sendSimpleMessage;

@RestController
@RequestMapping("api/")
public class LoanController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO prestamo, Authentication authentication){

        Optional<Loan> prestamoSolicitado =  loanRepository.findById(prestamo.getLoanId());
        Client client = this.clientRepository.findByEmail(authentication.getName());
        Account account = this.accountRepository.findByNumber(prestamo.getToAccountNumber());

        if(!prestamoSolicitado.isPresent()){
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }

        if(prestamo.getToAccountNumber().isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(prestamo.getLoanId() == 0 ){
            return new ResponseEntity<>("Debe escoger un tipo de prestamo", HttpStatus.FORBIDDEN);
        }

        if(prestamo.getAmount() == 0){
            return new ResponseEntity<>("Debe escoger una cantidad", HttpStatus.FORBIDDEN);
        }

        if(prestamo.getPayments() == 0){
            return new ResponseEntity<>("Debe escoger una cantidad de cuotas", HttpStatus.FORBIDDEN);
        }

        //El monto solicitado es mas grande que el monto máximo
        if(prestamo.getAmount() > prestamoSolicitado.get().getMaxAmount()){
            return new ResponseEntity<>("Debe escoger un monto máximo de: "+ prestamoSolicitado.get().getMaxAmount(), HttpStatus.FORBIDDEN);
        }

        //Verificar la cantidad de cuotas se encuentre disponible entre las del préstamo
        if(!prestamoSolicitado.get().getPayments().contains(prestamo.getPayments())){
            return new ResponseEntity<>("Debe escoger una cuota válida: ", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de destino exista
        if(account.getClient() == null){
            return new ResponseEntity<>("La cuenta de destino no existe: ", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de destino pertenezca al cliente autenticado
        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("La cuenta no pertenece al cliente: " + client.getFirstName() + " " + client.getLastName(), HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(prestamo.getAmount(), prestamo.getPayments(), client, prestamoSolicitado.get());
        clientLoanRepository.save(clientLoan);

        Transaction transactionCredito = new Transaction(TransactionType.CREDIT, prestamo.getAmount(), prestamoSolicitado.get().getName() + " loan approved", LocalDateTime.now(), account);
        transactionRepository.save(transactionCredito);

        account.setBalance(account.getBalance() + prestamo.getAmount());
        accountRepository.save(account);

        try {
            JsonNode jsonNode = sendSimpleMessage("Debuwin support dionisio.santis@gmail.com", client.getEmail(), "Prestamo aprobado", "Estimado "+ client.getFirstName()+" "+client.getLastName()+ " Ha solicitado un prestamo por un monto de $" + prestamo.getAmount()+" ,Felicitaciones!! Ha sido aprobado y depositado en tu cuenta Nro "+account.getNumber());
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        return new ResponseEntity<>("Prestamo creado con éxito", HttpStatus.CREATED);
    }



}
