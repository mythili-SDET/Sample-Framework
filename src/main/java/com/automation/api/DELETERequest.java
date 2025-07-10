package com.automation.api;

import com.automation.core.BaseAPITest;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * DELETE Request handler class
 * Provides specific functionality for DELETE requests
 */
public class DELETERequest extends BaseAPITest {
    private static final Logger logger = LogManager.getLogger(DELETERequest.class);

    /**
     * DELETE request
     */
    public Response delete(String endpoint) {
        logger.info("Making DELETE request to: {}", endpoint);
        return super.delete(endpoint);
    }

    /**
     * DELETE request with path parameters
     */
    public Response delete(String endpoint, Map<String, Object> pathParams) {
        logger.info("Making DELETE request to: {} with path params: {}", endpoint, pathParams);
        return super.delete(endpoint, pathParams);
    }

    /**
     * DELETE request with body
     */
    public Response deleteWithBody(String endpoint, Object body) {
        logger.info("Making DELETE request to: {} with body: {}", endpoint, body);
        return requestSpec.body(body).when().delete(endpoint);
    }

    /**
     * DELETE request with body and path parameters
     */
    public Response deleteWithBody(String endpoint, Object body, Map<String, Object> pathParams) {
        logger.info("Making DELETE request to: {} with body: {} and path params: {}", 
                   endpoint, body, pathParams);
        return requestSpec.body(body).pathParams(pathParams).when().delete(endpoint);
    }

    /**
     * DELETE request with headers
     */
    public Response deleteWithHeaders(String endpoint, Map<String, String> headers) {
        logger.info("Making DELETE request to: {} with headers: {}", endpoint, headers);
        return requestSpec.headers(headers).when().delete(endpoint);
    }

    /**
     * DELETE request with body and headers
     */
    public Response deleteWithBodyAndHeaders(String endpoint, Object body, Map<String, String> headers) {
        logger.info("Making DELETE request to: {} with body and headers: {}", endpoint, headers);
        return requestSpec.headers(headers).body(body).when().delete(endpoint);
    }

    /**
     * DELETE request with authentication
     */
    public Response deleteWithAuth(String endpoint, String token) {
        logger.info("Making DELETE request to: {} with authentication", endpoint);
        return requestSpec.header("Authorization", "Bearer " + token)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and authentication
     */
    public Response deleteWithAuth(String endpoint, Object body, String token) {
        logger.info("Making DELETE request to: {} with body and authentication", endpoint);
        return requestSpec.header("Authorization", "Bearer " + token)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with basic authentication
     */
    public Response deleteWithBasicAuth(String endpoint, String username, String password) {
        logger.info("Making DELETE request to: {} with basic authentication", endpoint);
        return requestSpec.auth().basic(username, password)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and basic authentication
     */
    public Response deleteWithBasicAuth(String endpoint, Object body, String username, String password) {
        logger.info("Making DELETE request to: {} with body and basic authentication", endpoint);
        return requestSpec.auth().basic(username, password)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with custom content type
     */
    public Response deleteWithContentType(String endpoint, String contentType) {
        logger.info("Making DELETE request to: {} with content type: {}", endpoint, contentType);
        return requestSpec.contentType(contentType)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and custom content type
     */
    public Response deleteWithContentType(String endpoint, Object body, String contentType) {
        logger.info("Making DELETE request to: {} with body and content type: {}", endpoint, contentType);
        return requestSpec.contentType(contentType)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with custom timeout
     */
    public Response deleteWithTimeout(String endpoint, int timeoutSeconds) {
        logger.info("Making DELETE request to: {} with timeout: {} seconds", endpoint, timeoutSeconds);
        return requestSpec.timeout(java.time.Duration.ofSeconds(timeoutSeconds))
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and custom timeout
     */
    public Response deleteWithTimeout(String endpoint, Object body, int timeoutSeconds) {
        logger.info("Making DELETE request to: {} with body and timeout: {} seconds", endpoint, timeoutSeconds);
        return requestSpec.timeout(java.time.Duration.ofSeconds(timeoutSeconds))
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with cookies
     */
    public Response deleteWithCookies(String endpoint, Map<String, String> cookies) {
        logger.info("Making DELETE request to: {} with cookies: {}", endpoint, cookies);
        return requestSpec.cookies(cookies)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and cookies
     */
    public Response deleteWithCookies(String endpoint, Object body, Map<String, String> cookies) {
        logger.info("Making DELETE request to: {} with body and cookies: {}", endpoint, cookies);
        return requestSpec.cookies(cookies)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with session
     */
    public Response deleteWithSession(String endpoint, String sessionId) {
        logger.info("Making DELETE request to: {} with session: {}", endpoint, sessionId);
        return requestSpec.sessionId(sessionId)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and session
     */
    public Response deleteWithSession(String endpoint, Object body, String sessionId) {
        logger.info("Making DELETE request to: {} with body and session: {}", endpoint, sessionId);
        return requestSpec.sessionId(sessionId)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with proxy
     */
    public Response deleteWithProxy(String endpoint, String host, int port) {
        logger.info("Making DELETE request to: {} with proxy: {}:{}", endpoint, host, port);
        return requestSpec.proxy(host, port)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and proxy
     */
    public Response deleteWithProxy(String endpoint, Object body, String host, int port) {
        logger.info("Making DELETE request to: {} with body and proxy: {}:{}", endpoint, host, port);
        return requestSpec.proxy(host, port)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with relaxed HTTPS validation
     */
    public Response deleteWithRelaxedHTTPS(String endpoint) {
        logger.info("Making DELETE request to: {} with relaxed HTTPS validation", endpoint);
        return requestSpec.relaxedHTTPSValidation()
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and relaxed HTTPS validation
     */
    public Response deleteWithRelaxedHTTPS(String endpoint, Object body) {
        logger.info("Making DELETE request to: {} with body and relaxed HTTPS validation", endpoint);
        return requestSpec.relaxedHTTPSValidation()
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with custom user agent
     */
    public Response deleteWithUserAgent(String endpoint, String userAgent) {
        logger.info("Making DELETE request to: {} with user agent: {}", endpoint, userAgent);
        return requestSpec.header("User-Agent", userAgent)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and custom user agent
     */
    public Response deleteWithUserAgent(String endpoint, Object body, String userAgent) {
        logger.info("Making DELETE request to: {} with body and user agent: {}", endpoint, userAgent);
        return requestSpec.header("User-Agent", userAgent)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with custom referer
     */
    public Response deleteWithReferer(String endpoint, String referer) {
        logger.info("Making DELETE request to: {} with referer: {}", endpoint, referer);
        return requestSpec.header("Referer", referer)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and custom referer
     */
    public Response deleteWithReferer(String endpoint, Object body, String referer) {
        logger.info("Making DELETE request to: {} with body and referer: {}", endpoint, referer);
        return requestSpec.header("Referer", referer)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with custom origin
     */
    public Response deleteWithOrigin(String endpoint, String origin) {
        logger.info("Making DELETE request to: {} with origin: {}", endpoint, origin);
        return requestSpec.header("Origin", origin)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and custom origin
     */
    public Response deleteWithOrigin(String endpoint, Object body, String origin) {
        logger.info("Making DELETE request to: {} with body and origin: {}", endpoint, origin);
        return requestSpec.header("Origin", origin)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with custom if match
     */
    public Response deleteWithIfMatch(String endpoint, String etag) {
        logger.info("Making DELETE request to: {} with if match: {}", endpoint, etag);
        return requestSpec.header("If-Match", etag)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and custom if match
     */
    public Response deleteWithIfMatch(String endpoint, Object body, String etag) {
        logger.info("Making DELETE request to: {} with body and if match: {}", endpoint, etag);
        return requestSpec.header("If-Match", etag)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with custom if none match
     */
    public Response deleteWithIfNoneMatch(String endpoint, String etag) {
        logger.info("Making DELETE request to: {} with if none match: {}", endpoint, etag);
        return requestSpec.header("If-None-Match", etag)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and custom if none match
     */
    public Response deleteWithIfNoneMatch(String endpoint, Object body, String etag) {
        logger.info("Making DELETE request to: {} with body and if none match: {}", endpoint, etag);
        return requestSpec.header("If-None-Match", etag)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with custom if modified since
     */
    public Response deleteWithIfModifiedSince(String endpoint, String date) {
        logger.info("Making DELETE request to: {} with if modified since: {}", endpoint, date);
        return requestSpec.header("If-Modified-Since", date)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and custom if modified since
     */
    public Response deleteWithIfModifiedSince(String endpoint, Object body, String date) {
        logger.info("Making DELETE request to: {} with body and if modified since: {}", endpoint, date);
        return requestSpec.header("If-Modified-Since", date)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with custom if unmodified since
     */
    public Response deleteWithIfUnmodifiedSince(String endpoint, String date) {
        logger.info("Making DELETE request to: {} with if unmodified since: {}", endpoint, date);
        return requestSpec.header("If-Unmodified-Since", date)
                         .when()
                         .delete(endpoint);
    }

    /**
     * DELETE request with body and custom if unmodified since
     */
    public Response deleteWithIfUnmodifiedSince(String endpoint, Object body, String date) {
        logger.info("Making DELETE request to: {} with body and if unmodified since: {}", endpoint, date);
        return requestSpec.header("If-Unmodified-Since", date)
                         .body(body)
                         .when()
                         .delete(endpoint);
    }
}