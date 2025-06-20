package com.hackathon.finservice.Controllers;

import com.hackathon.finservice.DTO.request.AccountCreationRequest;
import com.hackathon.finservice.DTO.request.FundTransferRequest;
import com.hackathon.finservice.DTO.request.TransactionRequest;
import com.hackathon.finservice.DTO.response.TransactionResponse;
import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Entities.Transaction;
import com.hackathon.finservice.Service.AccountService;
import com.hackathon.finservice.Service.TransactionService;
import com.hackathon.finservice.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AccountController {

    AccountService accountService;
    UserService userService;
    TransactionService transactionService;

    @PostMapping("/account/create")
    public ResponseEntity<String> createAccount(@RequestBody AccountCreationRequest request,
                                                @AuthenticationPrincipal UserDetails userDetails){
        Long userId = userService.checkOwnership(request.getAccountNumber(), userDetails.getUsername());
        accountService.createAccount(userId, request.getAccountType());
        return ResponseEntity.ok("New account added successfully for user");
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionRequest request,
                                                             @AuthenticationPrincipal UserDetails userDetails){
        Account account = userService.retrieveMainAccount(userDetails.getUsername());
        Transaction transaction = transactionService.deposit(account, request.getAmount());
        transactionService.monitorDeposit(transaction, account);
        return ResponseEntity.ok(new TransactionResponse("Fund transferred successfully"));
    }

    @PostMapping("/account/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody TransactionRequest request,
                                                       @AuthenticationPrincipal UserDetails userDetails){
        Account account = userService.retrieveMainAccount(userDetails.getUsername());
        Transaction transaction = transactionService.withdraw(account, request.getAmount());
        transactionService.monitorWithdraw(transaction, account);
        return ResponseEntity.ok(new TransactionResponse("Cash withdrawn successfully"));
    }

    @PostMapping("/account/fund-transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody FundTransferRequest request,
                                                        @AuthenticationPrincipal UserDetails userDetails){
        Account account = userService.retrieveMainAccount(userDetails.getUsername());
        Transaction transaction = transactionService.transfer(account, request.getTargetAccountNumber(),
                request.getAmount());
        transactionService.monitorTransfer(transaction, account);
        return ResponseEntity.ok(new TransactionResponse("Fund transferred successfully"));
    }

    @GetMapping("/account/transactions")
    public ResponseEntity<List<Transaction>> getHistory(@AuthenticationPrincipal UserDetails userDetails){
        Account account = userService.retrieveMainAccount(userDetails.getUsername());
        List<Transaction> transactions = transactionService.getHistory(account);
        return ResponseEntity.ok(transactions);
    }




}
