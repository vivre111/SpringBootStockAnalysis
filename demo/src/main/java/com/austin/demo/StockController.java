package com.austin.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {
    @Autowired
    JDBCController jdbcController;
    @GetMapping("stocks/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable("id")int id){
        Stock stock = jdbcController.findById(id);
        if (stock==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(stock, HttpStatus.OK);
        }
    }

}
