package com.leviberga.fluxpay.service;

import com.leviberga.fluxpay.dto.AuthorizationResponse;
import com.leviberga.fluxpay.exception.UnauthorizedTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthorizationService {

        private final RestTemplate restTemplate;

        public AuthorizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        }

        public boolean authorize() {
            String url = "https://util.devi.tools/api/v2/authorize";

            ResponseEntity<AuthorizationResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    AuthorizationResponse.class
            );
            if (!response.getBody().getData().getAuthorization()){
                throw new UnauthorizedTransactionException("Unauthorized");
            }
            else {
                return true;
            }
        }
    }
