package com.bcp.app.security;

import ma.bcp.service.AuthorizationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public List<String> getAuthorizations(String userLogin) {
        List<String> userAuthorizations = new ArrayList<>();
        userAuthorizations.add("CREER_CLIENT");
        userAuthorizations.add("CONSULTER_CLIENT");
        return userAuthorizations;
    }
}
