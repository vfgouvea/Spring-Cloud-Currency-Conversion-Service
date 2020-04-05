package com.conversionservice.controller;

import com.conversionservice.model.CurrencyConversion;
import com.conversionservice.proxy.CurrencyExchangeServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CurrencyExchangeServiceProxy proxy;

    @GetMapping("currency-converter/from/{from}/to/{to}/quantity/{qtd}")
    public CurrencyConversion getConversionValue(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal qtd){

        Map<String,String> uriVariables = new HashMap<>();
        uriVariables.put("from",from);
        uriVariables.put("to",to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}"
                ,CurrencyConversion.class,uriVariables);
        CurrencyConversion response = responseEntity.getBody();

    /*    Mono<CurrencyConversion> responseMono = WebClient.create()
                .get().uri("http://localhost:8000/currency-exchange/from/{from}/to/{to}",uriVariables)
                .retrieve().bodyToMono(CurrencyConversion.class);*/

        CurrencyConversion currencyConversion = new CurrencyConversion(response.getId(),from,to
                ,response.getConversionMultiple(),qtd,qtd.multiply(response.getConversionMultiple()),response.getPort());

        return currencyConversion;

    }

    @GetMapping("currency-converter-feign/from/{from}/to/{to}/quantity/{qtd}")
    public CurrencyConversion getConversionValueFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal qtd){

        CurrencyConversion currencyC = proxy.getExchangeValue(from,to);
        CurrencyConversion currencyConversion = new CurrencyConversion(currencyC.getId(),from,to
                ,currencyC.getConversionMultiple(),qtd,qtd.multiply(currencyC.getConversionMultiple()),currencyC.getPort());
        logger.info("{}",currencyConversion);
        return currencyConversion;

    }

}
