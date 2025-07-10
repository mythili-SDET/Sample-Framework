package com.automation.api;

import com.automation.core.BaseAPITest;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * GET Request handler class
 * Provides specific functionality for GET requests
 */
public class GETRequest extends BaseAPITest {
    private static final Logger logger = LogManager.getLogger(GETRequest.class);

    /**
     * GET request to endpoint
     */
    public Response get(String endpoint) {
        logger.info("Making GET request to: {}", endpoint);
        return super.get(endpoint);
    }

    /**
     * GET request with path parameters
     */
    public Response get(String endpoint, Map<String, Object> pathParams) {
        logger.info("Making GET request to: {} with path params: {}", endpoint, pathParams);
        return super.get(endpoint, pathParams);
    }

    /**
     * GET request with query parameters
     */
    public Response get(String endpoint, Map<String, Object> pathParams, Map<String, Object> queryParams) {
        logger.info("Making GET request to: {} with path params: {} and query params: {}", 
                   endpoint, pathParams, queryParams);
        return super.get(endpoint, pathParams, queryParams);
    }

    /**
     * GET request with query parameters only
     */
    public Response getWithQueryParams(String endpoint, Map<String, Object> queryParams) {
        logger.info("Making GET request to: {} with query params: {}", endpoint, queryParams);
        return requestSpec.queryParams(queryParams).when().get(endpoint);
    }

    /**
     * GET request with headers
     */
    public Response getWithHeaders(String endpoint, Map<String, String> headers) {
        logger.info("Making GET request to: {} with headers: {}", endpoint, headers);
        return requestSpec.headers(headers).when().get(endpoint);
    }

    /**
     * GET request with custom timeout
     */
    public Response getWithTimeout(String endpoint, int timeoutSeconds) {
        logger.info("Making GET request to: {} with timeout: {} seconds", endpoint, timeoutSeconds);
        return requestSpec.timeout(java.time.Duration.ofSeconds(timeoutSeconds))
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with authentication
     */
    public Response getWithAuth(String endpoint, String token) {
        logger.info("Making GET request to: {} with authentication", endpoint);
        return requestSpec.header("Authorization", "Bearer " + token)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with basic authentication
     */
    public Response getWithBasicAuth(String endpoint, String username, String password) {
        logger.info("Making GET request to: {} with basic authentication", endpoint);
        return requestSpec.auth().basic(username, password)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with digest authentication
     */
    public Response getWithDigestAuth(String endpoint, String username, String password) {
        logger.info("Making GET request to: {} with digest authentication", endpoint);
        return requestSpec.auth().digest(username, password)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with preemptive authentication
     */
    public Response getWithPreemptiveAuth(String endpoint, String username, String password) {
        logger.info("Making GET request to: {} with preemptive authentication", endpoint);
        return requestSpec.auth().preemptive().basic(username, password)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with form parameters
     */
    public Response getWithFormParams(String endpoint, Map<String, String> formParams) {
        logger.info("Making GET request to: {} with form params: {}", endpoint, formParams);
        return requestSpec.formParams(formParams)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with multipart
     */
    public Response getWithMultipart(String endpoint, String controlName, String filePath) {
        logger.info("Making GET request to: {} with multipart file: {}", endpoint, filePath);
        return requestSpec.multiPart(controlName, new java.io.File(filePath))
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with cookies
     */
    public Response getWithCookies(String endpoint, Map<String, String> cookies) {
        logger.info("Making GET request to: {} with cookies: {}", endpoint, cookies);
        return requestSpec.cookies(cookies)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with session
     */
    public Response getWithSession(String endpoint, String sessionId) {
        logger.info("Making GET request to: {} with session: {}", endpoint, sessionId);
        return requestSpec.sessionId(sessionId)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with proxy
     */
    public Response getWithProxy(String endpoint, String host, int port) {
        logger.info("Making GET request to: {} with proxy: {}:{}", endpoint, host, port);
        return requestSpec.proxy(host, port)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom SSL
     */
    public Response getWithSSL(String endpoint, String keystorePath, String keystorePassword) {
        logger.info("Making GET request to: {} with SSL keystore", endpoint);
        return requestSpec.keystore(keystorePath, keystorePassword)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with relaxed HTTPS validation
     */
    public Response getWithRelaxedHTTPS(String endpoint) {
        logger.info("Making GET request to: {} with relaxed HTTPS validation", endpoint);
        return requestSpec.relaxedHTTPSValidation()
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom content type
     */
    public Response getWithContentType(String endpoint, String contentType) {
        logger.info("Making GET request to: {} with content type: {}", endpoint, contentType);
        return requestSpec.contentType(contentType)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom accept header
     */
    public Response getWithAccept(String endpoint, String acceptType) {
        logger.info("Making GET request to: {} with accept: {}", endpoint, acceptType);
        return requestSpec.accept(acceptType)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom user agent
     */
    public Response getWithUserAgent(String endpoint, String userAgent) {
        logger.info("Making GET request to: {} with user agent: {}", endpoint, userAgent);
        return requestSpec.header("User-Agent", userAgent)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom referer
     */
    public Response getWithReferer(String endpoint, String referer) {
        logger.info("Making GET request to: {} with referer: {}", endpoint, referer);
        return requestSpec.header("Referer", referer)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom origin
     */
    public Response getWithOrigin(String endpoint, String origin) {
        logger.info("Making GET request to: {} with origin: {}", endpoint, origin);
        return requestSpec.header("Origin", origin)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom cache control
     */
    public Response getWithCacheControl(String endpoint, String cacheControl) {
        logger.info("Making GET request to: {} with cache control: {}", endpoint, cacheControl);
        return requestSpec.header("Cache-Control", cacheControl)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom if-none-match
     */
    public Response getWithIfNoneMatch(String endpoint, String etag) {
        logger.info("Making GET request to: {} with if-none-match: {}", endpoint, etag);
        return requestSpec.header("If-None-Match", etag)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom if-modified-since
     */
    public Response getWithIfModifiedSince(String endpoint, String date) {
        logger.info("Making GET request to: {} with if-modified-since: {}", endpoint, date);
        return requestSpec.header("If-Modified-Since", date)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom range
     */
    public Response getWithRange(String endpoint, String range) {
        logger.info("Making GET request to: {} with range: {}", endpoint, range);
        return requestSpec.header("Range", range)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom accept encoding
     */
    public Response getWithAcceptEncoding(String endpoint, String encoding) {
        logger.info("Making GET request to: {} with accept encoding: {}", endpoint, encoding);
        return requestSpec.header("Accept-Encoding", encoding)
                         .when()
                         .get(endpoint);
    }

    /**
     * GET request with custom accept language
     */
    public Response getWithAcceptLanguage(String endpoint, String language) {
        logger.info("Making GET request to: {} with accept language: {}", endpoint, language);
        return requestSpec.header("Accept-Language", language)
                         .when()
                         .get(endpoint);
    }
}