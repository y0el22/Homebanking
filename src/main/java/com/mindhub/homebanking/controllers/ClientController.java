package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static utils.SendEmail.sendSimpleMessage;

//El objetivo del controlador es recibir peticiones a través de métodos
@RestController
@RequestMapping("api/")
public class ClientController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
    //Esto estaba antes:
    //public List<Client> getClients(){
        //return clienRepository.findAll();
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

   @RequestMapping("/clients/{id}")
   public ClientDTO getClient (@PathVariable Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);

   }

    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);

    }

    @GetMapping("/clients/export")
    public void exportToPdf (HttpServletResponse response)throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime= dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);
        List<ClientDTO> listar= this.clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
        PDFExport exporter= new PDFExport(listar);
        exporter.export(response);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> createClient(@RequestParam String firstName,@RequestParam String lastName,
                                               @RequestParam String email,@RequestParam String password){

        //Si se deja algun campo vacío
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        Account account = new Account( "VIN" + (int)(Math.random() * (10000000-1)+1), LocalDateTime.now(), 0, client);
        accountRepository.save(account);

        try {
            JsonNode jsonNode = sendSimpleMessage("Debuwin support dionisio.santis@gmail.com",client.getEmail(),"Bienvenido a Nuestro banco","Estimado "+ client.getFirstName()+" "+client.getLastName()+" te confirmamos que has creado una cuenta en Debuwin, bienvenido!");
            System.out.println(jsonNode.toString());
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
