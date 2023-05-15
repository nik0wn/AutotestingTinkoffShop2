package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.example.components.AccountComponent;
import org.example.components.ProductComponent;
import org.example.entity.Account;
import org.example.entity.Product;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class AccountController {
    @Autowired
    AccountComponent accountComponent;
    @PutMapping("replenishmentBalance")
    @Operation(summary = "Пополнение баланса")
    public Account replenishmentBalance(
            @RequestParam String phone,
            @RequestParam double balance) {
        return accountComponent.putReplenishmentBalance(phone,balance);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessage> handleException(NoSuchElementException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }
}