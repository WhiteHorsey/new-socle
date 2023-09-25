package ma.bcp.controller;

import lombok.RequiredArgsConstructor;
import ma.bcp.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author bilalslayki
 */
@RestController
@RequestMapping("/authorization")
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationService autorizationService;

    @GetMapping(value = "/{userLogin}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<String> find(@PathVariable String userLogin) {
        return this.autorizationService.getAuthorizations(userLogin);
    }

}