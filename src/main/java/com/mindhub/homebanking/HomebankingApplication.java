package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;

//Es la clase principal

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	//CommandLineRunner agrega data al ClienRepository
	//De esta forma agregamos data interna y no desde la consola

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  ClientLoanRepository clientLoanRepository,
									  LoanRepository loanRepository,
									  CardRepository cardRepository,
	                                  SeguroRepository seguroRepository,
									  ClientSeguroRepository clientSeguroRepository) {
		return (args) -> {

			//Clientes
			Client cliente1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba"));
			Client cliente2 = new Client("Andres", "Oyarzun", "andres@gmail.com", passwordEncoder.encode("contrasenaAndres"));
			//Se guardan los clientes en el repositorio de clientes
			clientRepository.save(cliente1);
			clientRepository.save(cliente2);


			//Cuentas
			//Creamos cuentas, y estas las asociamos a los clientes:
			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000, cliente1);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500, cliente1);
			Account account3 = new Account("VIN003", LocalDateTime.now(), 5000, cliente2);
			Account account4 = new Account("VIN004", LocalDateTime.now(), 7500, cliente2);
			//Guardamos la cuentas en el repositorio de cuentas
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);


			//Transacciones
			//Creamos transacciones de débito y/o crédito, las cuales se asocian a una cuenta
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1000, "Compra de pan", LocalDateTime.now(), account1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -2000, "Compra gas", LocalDateTime.now(), account1);
			//Guardamos las transacciones en el repositorio de transacciones
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);


			//Tipos de prestamos
			Loan loan1 = new Loan("Hipotecario", 500000, Arrays.asList(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
			Loan loan3 = new Loan("Automotriz", 300000, Arrays.asList(6, 12, 24, 36));
			//Guarda los tipos de prestamos en el repositorio loan*//*
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);


			//Prestamo asociado a los clientes
			//Melba
			ClientLoan clientLoan1 = new ClientLoan(400000, 60, cliente1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12, cliente1, loan2);
			//El otro cliente:
			ClientLoan clientLoan3 = new ClientLoan(100000, 24, cliente2, loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36, cliente2, loan3);
			//Guardamos los prestamos asociados a los clientes en el repositorio client-loan
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);


			//Tarjetas
			//Añadimos las tarjetas
			Card card1 = new Card("Melba Morel", CardType.DEBIT, CardColor.GOLD, "1234 5678 9123 4567", 123, LocalDateTime.now(), LocalDateTime.now().plusYears(5), cliente1);
			Card card2 = new Card("Melba Morel", CardType.CREDIT, CardColor.TITANIUM, "5133 5855 2685 2479", 992, LocalDateTime.now(), LocalDateTime.now().plusYears(5), cliente1);
			Card card3 = new Card(cliente2.getFirstName() + " " + cliente2.getLastName(), CardType.CREDIT, CardColor.SILVER, "5312 3206 3807 1484", 879, LocalDateTime.now(), LocalDateTime.now().plusYears(5), cliente2);
			//Agregamos las tarjetas al repo card.
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);


			//Tipos de seguros
			Seguro seguro1 = new Seguro("Seguro de vida", 2390);
			Seguro seguro2 = new Seguro("Seguro auto", 5500);
			Seguro seguro3 = new Seguro("Seguro casa", 1250);
			//Guarda los tipos de seguros en el repositorio de seguro
			seguroRepository.save(seguro1);
			seguroRepository.save(seguro2);
			seguroRepository.save(seguro3);


			ClientSeguro clientSeguro1 = new ClientSeguro(500, cliente1, seguro1);
			clientSeguroRepository.save(clientSeguro1);
		};
	}
}
