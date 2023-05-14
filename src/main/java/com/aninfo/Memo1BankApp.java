package com.aninfo;

import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.exceptions.invalidIdTransaction;
import com.aninfo.exceptions.invalidTypeOfTransaction;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.service.AccountService;
import com.aninfo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@SpringBootApplication
@EnableSwagger2
public class Memo1BankApp {

	@Autowired
	private AccountService accountService;
	@Autowired
	private TransactionService transactionService;

	public static void main(String[] args) {
		SpringApplication.run(Memo1BankApp.class, args);
	}

	@PostMapping("/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	public Account createAccount(@RequestBody Account account) {
		return accountService.createAccount(account);
	}

	@GetMapping("/accounts")
	public Collection<Account> getAccounts() {
		return accountService.getAccounts();
	}

	@GetMapping("/accounts/{cbu}")
	public ResponseEntity<Account> getAccount(@PathVariable Long cbu) {
		Optional<Account> accountOptional = accountService.findById(cbu);
		return ResponseEntity.of(accountOptional);
	}

	@PutMapping("/accounts/{cbu}")
	public ResponseEntity<Account> updateAccount(@RequestBody Account account, @PathVariable Long cbu) {
		Optional<Account> accountOptional = accountService.findById(cbu);

		if (!accountOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		account.setCbu(cbu);
		accountService.save(account);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/accounts/{cbu}")
	public void deleteAccount(@PathVariable Long cbu) {
		accountService.deleteById(cbu);
	}

	@PutMapping("/accounts/{cbu}/withdraw")
	public Transaction withdraw(@PathVariable Long cbu, @RequestParam Double sum) {
		Transaction transaction = new Transaction("withdraw", sum, cbu);
		accountService.withdraw(cbu, transaction.getAmount());
		return transactionService.withdraw(transaction);
	}

	@PutMapping("/accounts/{cbu}/deposit")
	public Transaction deposit(@PathVariable Long cbu, @RequestParam Double sum) {
		Transaction transaction = new Transaction("deposit", sum, cbu);
		accountService.deposit(cbu, transaction.getAmount());
		return transactionService.deposit(transaction);
	}

	 @PostMapping("/transactions")
	 @ResponseStatus(HttpStatus.CREATED)
	 public Transaction createTransaction(@RequestBody Transaction transaction) {

		if(transaction.getType().equals("deposit")) {
			accountService.deposit(transaction.getCbu(), transaction.getAmount());
			return transactionService.deposit(transaction);
		} else if (transaction.getType().equals("withdraw")) {
			accountService.withdraw(transaction.getCbu(), transaction.getAmount());
			return transactionService.withdraw(transaction);
		}

		throw new invalidTypeOfTransaction("Invalid type of transaction");

	 }

	 @GetMapping("/accounts/transactions")
	 public List<Transaction> getTransactions(@RequestParam Long cbu) {
		return transactionService.getTransactionsByCbu(cbu);
	 }

	@GetMapping("/transactions/{transactionID}")
	public Transaction getTransaction(@PathVariable Long transactionID) {
		return transactionService.findTransactionByID(transactionID);

	}

	@DeleteMapping("/transactions/{transactionID}")
	public void deleteTransaction(@PathVariable Long transactionID) {
		Transaction transaction = transactionService.findTransactionByID(transactionID);

		if (transaction == null) {
			throw new invalidIdTransaction("Invalid ID of the transaction");
		}
		transactionService.deleteTransaction(transaction);
	}

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.any())
			.build();
	}
}
