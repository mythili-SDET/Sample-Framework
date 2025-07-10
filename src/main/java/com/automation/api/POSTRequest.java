package com.automation.api;

import com.automation.core.BaseAPITest;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * POST Request handler class
 * Provides specific functionality for POST requests
 */
public class POSTRequest extends BaseAPITest {
    private static final Logger logger = LogManager.getLogger(POSTRequest.class);

    /**
     * POST request with body
     */
    public Response post(String endpoint, Object body) {
        logger.info("Making POST request to: {} with body: {}", endpoint, body);
        return super.post(endpoint, body);
    }

    /**
     * POST request with body and path parameters
     */
    public Response post(String endpoint, Object body, Map<String, Object> pathParams) {
        logger.info("Making POST request to: {} with body: {} and path params: {}", 
                   endpoint, body, pathParams);
        return super.post(endpoint, body, pathParams);
    }

    /**
     * POST request with form parameters
     */
    public Response postWithFormParams(String endpoint, Map<String, String> formParams) {
        logger.info("Making POST request to: {} with form params: {}", endpoint, formParams);
        return requestSpec.formParams(formParams).when().post(endpoint);
    }

    /**
     * POST request with multipart
     */
    public Response postWithMultipart(String endpoint, String controlName, String filePath) {
        logger.info("Making POST request to: {} with multipart file: {}", endpoint, filePath);
        return requestSpec.multiPart(controlName, new java.io.File(filePath))
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with multiple files
     */
    public Response postWithMultipleFiles(String endpoint, Map<String, String> files) {
        logger.info("Making POST request to: {} with multiple files: {}", endpoint, files);
        for (Map.Entry<String, String> entry : files.entrySet()) {
            requestSpec.multiPart(entry.getKey(), new java.io.File(entry.getValue()));
        }
        return requestSpec.when().post(endpoint);
    }

    /**
     * POST request with headers
     */
    public Response postWithHeaders(String endpoint, Object body, Map<String, String> headers) {
        logger.info("Making POST request to: {} with headers: {}", endpoint, headers);
        return requestSpec.headers(headers).body(body).when().post(endpoint);
    }

    /**
     * POST request with authentication
     */
    public Response postWithAuth(String endpoint, Object body, String token) {
        logger.info("Making POST request to: {} with authentication", endpoint);
        return requestSpec.header("Authorization", "Bearer " + token)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with basic authentication
     */
    public Response postWithBasicAuth(String endpoint, Object body, String username, String password) {
        logger.info("Making POST request to: {} with basic authentication", endpoint);
        return requestSpec.auth().basic(username, password)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom content type
     */
    public Response postWithContentType(String endpoint, Object body, String contentType) {
        logger.info("Making POST request to: {} with content type: {}", endpoint, contentType);
        return requestSpec.contentType(contentType)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom timeout
     */
    public Response postWithTimeout(String endpoint, Object body, int timeoutSeconds) {
        logger.info("Making POST request to: {} with timeout: {} seconds", endpoint, timeoutSeconds);
        return requestSpec.timeout(java.time.Duration.ofSeconds(timeoutSeconds))
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with cookies
     */
    public Response postWithCookies(String endpoint, Object body, Map<String, String> cookies) {
        logger.info("Making POST request to: {} with cookies: {}", endpoint, cookies);
        return requestSpec.cookies(cookies)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with session
     */
    public Response postWithSession(String endpoint, Object body, String sessionId) {
        logger.info("Making POST request to: {} with session: {}", endpoint, sessionId);
        return requestSpec.sessionId(sessionId)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with proxy
     */
    public Response postWithProxy(String endpoint, Object body, String host, int port) {
        logger.info("Making POST request to: {} with proxy: {}:{}", endpoint, host, port);
        return requestSpec.proxy(host, port)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with relaxed HTTPS validation
     */
    public Response postWithRelaxedHTTPS(String endpoint, Object body) {
        logger.info("Making POST request to: {} with relaxed HTTPS validation", endpoint);
        return requestSpec.relaxedHTTPSValidation()
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom user agent
     */
    public Response postWithUserAgent(String endpoint, Object body, String userAgent) {
        logger.info("Making POST request to: {} with user agent: {}", endpoint, userAgent);
        return requestSpec.header("User-Agent", userAgent)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom referer
     */
    public Response postWithReferer(String endpoint, Object body, String referer) {
        logger.info("Making POST request to: {} with referer: {}", endpoint, referer);
        return requestSpec.header("Referer", referer)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom origin
     */
    public Response postWithOrigin(String endpoint, Object body, String origin) {
        logger.info("Making POST request to: {} with origin: {}", endpoint, origin);
        return requestSpec.header("Origin", origin)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom content length
     */
    public Response postWithContentLength(String endpoint, Object body, int contentLength) {
        logger.info("Making POST request to: {} with content length: {}", endpoint, contentLength);
        return requestSpec.header("Content-Length", String.valueOf(contentLength))
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom expect header
     */
    public Response postWithExpect(String endpoint, Object body, String expect) {
        logger.info("Making POST request to: {} with expect: {}", endpoint, expect);
        return requestSpec.header("Expect", expect)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom transfer encoding
     */
    public Response postWithTransferEncoding(String endpoint, Object body, String encoding) {
        logger.info("Making POST request to: {} with transfer encoding: {}", endpoint, encoding);
        return requestSpec.header("Transfer-Encoding", encoding)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom content encoding
     */
    public Response postWithContentEncoding(String endpoint, Object body, String encoding) {
        logger.info("Making POST request to: {} with content encoding: {}", endpoint, encoding);
        return requestSpec.header("Content-Encoding", encoding)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom content language
     */
    public Response postWithContentLanguage(String endpoint, Object body, String language) {
        logger.info("Making POST request to: {} with content language: {}", endpoint, language);
        return requestSpec.header("Content-Language", language)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom content disposition
     */
    public Response postWithContentDisposition(String endpoint, Object body, String disposition) {
        logger.info("Making POST request to: {} with content disposition: {}", endpoint, disposition);
        return requestSpec.header("Content-Disposition", disposition)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom content range
     */
    public Response postWithContentRange(String endpoint, Object body, String range) {
        logger.info("Making POST request to: {} with content range: {}", endpoint, range);
        return requestSpec.header("Content-Range", range)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom if match
     */
    public Response postWithIfMatch(String endpoint, Object body, String etag) {
        logger.info("Making POST request to: {} with if match: {}", endpoint, etag);
        return requestSpec.header("If-Match", etag)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom if none match
     */
    public Response postWithIfNoneMatch(String endpoint, Object body, String etag) {
        logger.info("Making POST request to: {} with if none match: {}", endpoint, etag);
        return requestSpec.header("If-None-Match", etag)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom if modified since
     */
    public Response postWithIfModifiedSince(String endpoint, Object body, String date) {
        logger.info("Making POST request to: {} with if modified since: {}", endpoint, date);
        return requestSpec.header("If-Modified-Since", date)
                         .body(body)
                         .when()
                         .post(endpoint);
    }

    /**
     * POST request with custom if unmodified since
     */
    public Response postWithIfUnmodifiedSince(String endpoint, Object body, String date) {
        logger.info("Making POST request to: {} with if unmodified since: {}", endpoint, date);
        return requestSpec.header("If-Unmodified-Since", date)
                         .body(body)
                         .when()
                         .post(endpoint);
    }
}