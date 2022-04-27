package com.zoopluscase.cryptocurrencyconverter.controller;

import com.zoopluscase.cryptocurrencyconverter.client.coinmarketcap.CoinMarketCapClientException;
import com.zoopluscase.cryptocurrencyconverter.client.ipapi.IpApiClientException;
import com.zoopluscase.cryptocurrencyconverter.model.ConvertRequestDTO;
import com.zoopluscase.cryptocurrencyconverter.model.ConvertResponseDTO;
import com.zoopluscase.cryptocurrencyconverter.service.CryptoCurrencyConversionService;
import com.zoopluscase.cryptocurrencyconverter.service.coinmarketcap.CoinMarketCapServiceException;
import com.zoopluscase.cryptocurrencyconverter.service.ipapi.IpApiValidationException;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@Log4j2
public class CryptoCurrencyConverterController {

    private static final Logger logger = LoggerFactory.getLogger(CryptoCurrencyConverterController.class);

    @Autowired
    private  CryptoCurrencyConversionService cryptoCurrencyConversionService;

    @GetMapping("/convert")
    public String convert(@ModelAttribute ConvertRequestDTO requestDTO, HttpServletRequest httpServletRequest, Model model) {

        model.addAttribute("requestDTO", requestDTO);
        ConvertResponseDTO responseDTO = new ConvertResponseDTO();

        if (requestDTO.getCoinSymbol() == null) {
            model.addAttribute("responseDTO", responseDTO);
            return "cryptoConversion";
        }
        logger.info("cryptoCurrency conversion Started");
        try {
            responseDTO = cryptoCurrencyConversionService.convertCryptoCurrency(requestDTO, httpServletRequest);
            model.addAttribute("responseDTO", responseDTO);
        } catch (IpApiClientException | CoinMarketCapClientException | IpApiValidationException | URISyntaxException | IOException | CoinMarketCapServiceException e) {
            logger.info("cryptoCurrency conversion failed ",e.getMessage());
            model.addAttribute("error", e.getMessage());
        }

        return "cryptoConversion";
    }
}
