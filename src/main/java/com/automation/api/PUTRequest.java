package com.automation.api;

import com.automation.core.BaseAPITest;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * PUT Request handler class
 * Provides specific functionality for PUT requests
 */
public class PUTRequest extends BaseAPITest {
    private static final Logger logger = LogManager.getLogger(PUTRequest.class);

    /**
     * PUT request with body
     */
    public Response put(String endpoint, Object body) {
        logger.info("Making PUT request to: {} with body: {}", endpoint, body);
        return super.put(endpoint, body);
    }

    /**
     * PUT request with body and path parameters
     */
    public Response put(String endpoint, Object body, Map<String, Object> pathParams) {
        logger.info("Making PUT request to: {} with body: {} and path params: {}", 
                   endpoint, body, pathParams);
        return super.put(endpoint, body, pathParams);
    }

    /**
     * PUT request with headers
     */
    public Response putWithHeaders(String endpoint, Object body, Map<String, String> headers) {
        logger.info("Making PUT request to: {} with headers: {}", endpoint, headers);
        return requestSpec.headers(headers).body(body).when().put(endpoint);
    }

    /**
     * PUT request with authentication
     */
    public Response putWithAuth(String endpoint, Object body, String token) {
        logger.info("Making PUT request to: {} with authentication", endpoint);
        return requestSpec.header("Authorization", "Bearer " + token)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with basic authentication
     */
    public Response putWithBasicAuth(String endpoint, Object body, String username, String password) {
        logger.info("Making PUT request to: {} with basic authentication", endpoint);
        return requestSpec.auth().basic(username, password)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom content type
     */
    public Response putWithContentType(String endpoint, Object body, String contentType) {
        logger.info("Making PUT request to: {} with content type: {}", endpoint, contentType);
        return requestSpec.contentType(contentType)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom timeout
     */
    public Response putWithTimeout(String endpoint, Object body, int timeoutSeconds) {
        logger.info("Making PUT request to: {} with timeout: {} seconds", endpoint, timeoutSeconds);
        return requestSpec.timeout(java.time.Duration.ofSeconds(timeoutSeconds))
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with cookies
     */
    public Response putWithCookies(String endpoint, Object body, Map<String, String> cookies) {
        logger.info("Making PUT request to: {} with cookies: {}", endpoint, cookies);
        return requestSpec.cookies(cookies)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with session
     */
    public Response putWithSession(String endpoint, Object body, String sessionId) {
        logger.info("Making PUT request to: {} with session: {}", endpoint, sessionId);
        return requestSpec.sessionId(sessionId)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with proxy
     */
    public Response putWithProxy(String endpoint, Object body, String host, int port) {
        logger.info("Making PUT request to: {} with proxy: {}:{}", endpoint, host, port);
        return requestSpec.proxy(host, port)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with relaxed HTTPS validation
     */
    public Response putWithRelaxedHTTPS(String endpoint, Object body) {
        logger.info("Making PUT request to: {} with relaxed HTTPS validation", endpoint);
        return requestSpec.relaxedHTTPSValidation()
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom user agent
     */
    public Response putWithUserAgent(String endpoint, Object body, String userAgent) {
        logger.info("Making PUT request to: {} with user agent: {}", endpoint, userAgent);
        return requestSpec.header("User-Agent", userAgent)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom referer
     */
    public Response putWithReferer(String endpoint, Object body, String referer) {
        logger.info("Making PUT request to: {} with referer: {}", endpoint, referer);
        return requestSpec.header("Referer", referer)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom origin
     */
    public Response putWithOrigin(String endpoint, Object body, String origin) {
        logger.info("Making PUT request to: {} with origin: {}", endpoint, origin);
        return requestSpec.header("Origin", origin)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom content length
     */
    public Response putWithContentLength(String endpoint, Object body, int contentLength) {
        logger.info("Making PUT request to: {} with content length: {}", endpoint, contentLength);
        return requestSpec.header("Content-Length", String.valueOf(contentLength))
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom expect header
     */
    public Response putWithExpect(String endpoint, Object body, String expect) {
        logger.info("Making PUT request to: {} with expect: {}", endpoint, expect);
        return requestSpec.header("Expect", expect)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom transfer encoding
     */
    public Response putWithTransferEncoding(String endpoint, Object body, String encoding) {
        logger.info("Making PUT request to: {} with transfer encoding: {}", endpoint, encoding);
        return requestSpec.header("Transfer-Encoding", encoding)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom content encoding
     */
    public Response putWithContentEncoding(String endpoint, Object body, String encoding) {
        logger.info("Making PUT request to: {} with content encoding: {}", endpoint, encoding);
        return requestSpec.header("Content-Encoding", encoding)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom content language
     */
    public Response putWithContentLanguage(String endpoint, Object body, String language) {
        logger.info("Making PUT request to: {} with content language: {}", endpoint, language);
        return requestSpec.header("Content-Language", language)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom content disposition
     */
    public Response putWithContentDisposition(String endpoint, Object body, String disposition) {
        logger.info("Making PUT request to: {} with content disposition: {}", endpoint, disposition);
        return requestSpec.header("Content-Disposition", disposition)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom content range
     */
    public Response putWithContentRange(String endpoint, Object body, String range) {
        logger.info("Making PUT request to: {} with content range: {}", endpoint, range);
        return requestSpec.header("Content-Range", range)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom if match
     */
    public Response putWithIfMatch(String endpoint, Object body, String etag) {
        logger.info("Making PUT request to: {} with if match: {}", endpoint, etag);
        return requestSpec.header("If-Match", etag)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom if none match
     */
    public Response putWithIfNoneMatch(String endpoint, Object body, String etag) {
        logger.info("Making PUT request to: {} with if none match: {}", endpoint, etag);
        return requestSpec.header("If-None-Match", etag)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom if modified since
     */
    public Response putWithIfModifiedSince(String endpoint, Object body, String date) {
        logger.info("Making PUT request to: {} with if modified since: {}", endpoint, date);
        return requestSpec.header("If-Modified-Since", date)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom if unmodified since
     */
    public Response putWithIfUnmodifiedSince(String endpoint, Object body, String date) {
        logger.info("Making PUT request to: {} with if unmodified since: {}", endpoint, date);
        return requestSpec.header("If-Unmodified-Since", date)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom content location
     */
    public Response putWithContentLocation(String endpoint, Object body, String location) {
        logger.info("Making PUT request to: {} with content location: {}", endpoint, location);
        return requestSpec.header("Content-Location", location)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom last modified
     */
    public Response putWithLastModified(String endpoint, Object body, String lastModified) {
        logger.info("Making PUT request to: {} with last modified: {}", endpoint, lastModified);
        return requestSpec.header("Last-Modified", lastModified)
                         .body(body)
                         .when()
                         .put(endpoint);
    }

    /**
     * PUT request with custom etag
     */
    public Response putWithETag(String endpoint, Object body, String etag) {
        logger.info("Making PUT request to: {} with etag: {}", endpoint, etag);
        return requestSpec.header("ETag", etag)
                         .body(body)
                         .when()
                         .put(endpoint);
    }
}