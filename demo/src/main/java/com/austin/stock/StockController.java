package com.austin.stock;

import com.austin.stock.kafka.KafkaSender;
import com.austin.stock.kafka.KafkaSenderWithMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@RestController
@Controller
public class StockController {
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private KafkaSenderWithMessageConverter messageConverter;
    @Value("${io.reflectoring.kafka.topic}")
    private String topic;
    @Autowired
    JDBCController jdbcController;
    @GetMapping("stocks")
    public ResponseEntity<Stock> getStockById(@RequestParam(value = "id", required = false)int id){
        Stock stock = jdbcController.findById(id);
        if (stock==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(stock, HttpStatus.OK);
        }
    }
    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal OidcUser principal) {
        return "index";
    }

    @ModelAttribute("profile")
    public Map<String, Object> addProfileAttribute(@AuthenticationPrincipal OidcUser principal) {
        return principal != null ? principal.getClaims() : null;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("stocks/getStockByName")
    public String byName(Model model){
        return "getStockByName";
    }

    @GetMapping("stocks/filter")
    public ResponseEntity<List<Stock>>getStockByName(@RequestParam("name")String name){
        name+=".csv";
        List<Stock> stock = jdbcController.findByName(name);
        if (stock.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(stock, HttpStatus.OK);
        }
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required = false, defaultValue = "world")String name, Model model){
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("stocks/send")
    public String send(Model model){
        return "send";
    }
    @PostMapping("/stocks/send")
    public String send(@RequestParam("stock") String stockname,Model model) {
        stockname+=".csv";
        List<Stock> stocks = jdbcController.findByName(stockname);
        if (stocks.isEmpty()){
            return "error";
        }else {
            for(Stock stockdata:stocks){
                messageConverter.sendMessageWithConverter(new GenericMessage<>(stockdata));
                kafkaSender.sendStock(stockdata, topic);
            }
            return "send";
        }
    }


}
