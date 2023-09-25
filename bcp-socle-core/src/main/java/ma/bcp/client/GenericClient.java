package ma.bcp.client;

import lombok.extern.slf4j.Slf4j;
import ma.bcp.utils.CoreUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


import static java.nio.charset.StandardCharsets.US_ASCII;
import static ma.bcp.utils.Constants.Security.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public class GenericClient<S, D> extends CoreClient {
    private final Class<D> type;
    private final Class<D[]> arrayType;

    public GenericClient(Class<D> type, Class<D[]> arrayType) {
        this.type = type;
        this.arrayType = arrayType;
    }

    @Autowired
    private RestTemplate restTemplate;

    // ------------------------------------- Generic Methods -------------------------------------
    public ResponseEntity<D> genericCall(S request, String url, HttpMethod method) {
        HttpEntity<S> entity = new HttpEntity<>(request);
        return restTemplate.exchange(CoreUtils.parseURI(url), method, entity, type);
    }

    public ResponseEntity<D> genericCallWithToken(S request, String url, HttpMethod method) {
        HttpEntity<S> entity = new HttpEntity<>(request, createTokenAuthHeaders());
        return restTemplate.exchange(CoreUtils.parseURI(url), method, entity, type);
    }

    public ResponseEntity<D> genericCallWithBasic(S request, HttpMethod method, String url, String username, String password) {
        HttpEntity<S> entity = new HttpEntity<>(request, createBasicAuthHeaders(username, password));
        return restTemplate.exchange(CoreUtils.parseURI(url), method, entity, type);
    }

    // ------------------------------------- Public data -------------------------------------
    public ResponseEntity<D> getData(String url) {
        return restTemplate.exchange(CoreUtils.parseURI(url), GET, null, type);
    }

    public ResponseEntity<D> postData(S request, String url) {
        return genericCall(request, url, POST);
    }

    public ResponseEntity<D> updateData(S request, String url) {
        return genericCall(request, url, PUT);
    }

    public ResponseEntity<D> deleteData(S request, String url) {
        return genericCall(request, url, DELETE);
    }

    // ------------------------------------- Secured Data With Token -------------------------------------
    public ResponseEntity<D> getSecuredDataWithToken(String url) {
        HttpEntity<S> entity = new HttpEntity<>(createTokenAuthHeaders());
        return restTemplate.exchange(CoreUtils.parseURI(url), GET, entity, type);
    }

    public ResponseEntity<D> postSecuredDataWithToken(S request, String url) {
        return genericCallWithToken(request, url, POST);
    }

    public ResponseEntity<D> updateSecuredDataWithToken(S request, String url) {
        return genericCallWithToken(request, url, PUT);
    }

    public ResponseEntity<D> deleteSecuredDataWithToken(S request, String url) {
        return genericCallWithToken(request, url, DELETE);
    }

    // ------------------------------------- Secured Data With Basic -------------------------------------
    public ResponseEntity<D> getSecuredDataWithBasic(String url, String username, String password) {
        HttpEntity<S> entity = new HttpEntity<>(createBasicAuthHeaders(username, password));
        return restTemplate.exchange(CoreUtils.parseURI(url), GET, entity, type);
    }

    public ResponseEntity<D> postSecuredDataWithBasic(S request, String url, String username, String password) {
        return genericCallWithBasic(request, POST, url, username, password);
    }

    public ResponseEntity<D> putSecuredDataWithBasic(S request, String url, String username, String password) {
        return genericCallWithBasic(request, PUT, url, username, password);
    }

    public ResponseEntity<D> deleteSecuredDataWithBasic(S request, String url, String username, String password) {
        return genericCallWithBasic(request, DELETE, url, username, password);
    }

    // ------------------------------------- Utils -------------------------------------
    public HttpHeaders createBasicAuthHeaders(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(US_ASCII));
        headers.set("Authorization", BASIC_PREFIX + new String(encodedAuth));
        headers.setAccept(Collections.singletonList(APPLICATION_JSON));
        return headers;
    }

    public HttpHeaders createTokenAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", TOKEN_PREFIX + getToken());
        headers.set("isKeycloakToken", TOKEN_PREFIX + getToken());
        headers.setAccept(Collections.singletonList(APPLICATION_JSON));
        return headers;
    }
}