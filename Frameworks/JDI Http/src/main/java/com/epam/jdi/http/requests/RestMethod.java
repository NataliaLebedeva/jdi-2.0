package com.epam.jdi.http.requests;

import com.epam.jdi.http.ExceptionHandler;
import com.epam.jdi.tools.func.JAction1;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.specification.RequestSpecification;

import java.util.List;

import static com.epam.jdi.http.requests.ResponseStatusType.OK;
import static com.epam.jdi.http.requests.RestMethodTypes.*;
import static com.jayway.restassured.RestAssured.given;
import static java.lang.System.currentTimeMillis;


/**
 * Created by Roman_Iovlev on 12/19/2016.
 */
public class RestMethod {
    public RequestSpecification spec = given();
    private RequestData data;
    private RestMethodTypes type;

    public RestMethod() {}
    public RestMethod(JAction1<RequestSpecification> specFunc, RestMethodTypes type) {
        specFunc.execute(spec);
        this.type = type;
    }
    public RestMethod(String url, RestMethodTypes type) {
        data = new RequestData().set(d -> d.url = url);
        this.type = type;
    }
    public void addHeader(String name, String value) {
        data.headers.add(new Header(name, value));
        spec.header(new Header(name, value));
    }
    public void addHeader(com.epam.jdi.http.annotations.Header header) {
        addHeader(header.name(), header.value());
    }
    public void addHeader(Header header) {
        addHeader(header.getName(), header.getValue());
    }
    public void addHeaders(List<com.epam.jdi.http.annotations.Header> headers) {
        for(com.epam.jdi.http.annotations.Header header : headers)
            addHeader(header);
    }
    public void addHeaders(Header[] headers) {
        for(Header header : headers)
            addHeader(header);
    }
    /*public RestMethod(String url) {
        this(url, null, null, null);
    }
    public RestMethod(String url, Headers header, RestMethodTypes type, String body) {
        spec = spec.baseUri(url).headers(header).body(body);
        this.data = type;
    }
    public RestMethod(RequestData data) {
        this(data.uri, null, data.type, null);
    }
    */

    public RestResponse call() {
        if (type == null)
            throw ExceptionHandler.exception("HttpMethodType not specified");
        return RestRequest.doRequest(type, getSpec());
    }
    public RestResponse call(String... params) {
        data.url = String.format(data.url, params);
        return call();
    }
    private RequestSpecification getSpec() {
        if (data == null)
            return spec;
        if (data.url != null)
            spec.baseUri(data.url);
        if (data.body != null)
            spec.body(data.body);
        return spec;
    }
    /*
    public RestResponse call(RequestParams params) {
        return callMethod(params);
    }
    public RestResponse call(String... params) {
        return call(new RequestParams(params));
    }
    public RestResponse call(UrlParams params) {
        return call(new RequestParams(params));
    }
    public RestResponse call(Headers params) {
        return call(new RequestParams(params));
    }*/
    public RestResponse GET() {
        return RestRequest.doRequest(GET, spec);
    }/*
    public RestResponse GET(RequestParams params) {
        return doRequest(GET, params);
    }
    public RestResponse GET(String... params) {
        return GET(new RequestParams(params));
    }
    public RestResponse GET(UrlParams params) {
        return GET(new RequestParams(params));
    }
    public RestResponse GET(Headers params) {
        return GET(new RequestParams(params));
    }
    */
    public RestResponse POST() {
        return RestRequest.doRequest(POST, spec);
    }
    /*
    public RestResponse POST(RequestParams params) {
        return PostRequest(url, params);
    }
    public RestResponse POST(String... params) {
        return POST(new RequestParams(params));
    }
    public RestResponse POST(UrlParams params) {
        return POST(new RequestParams(params));
    }
    public RestResponse POST(Headers params) {
        return POST(new RequestParams(params));
    }
    */
    public RestResponse PUT() {
        return RestRequest.doRequest(PUT, spec);
    }
    /*
    public RestResponse PUT(RequestParams params) {
        return PutRequest(url, params);
    }
    public RestResponse PUT(String... params) {
        return PUT(new RequestParams(params));
    }
    public RestResponse PUT(UrlParams params) {
        return PUT(new RequestParams(params));
    }
    public RestResponse PUT(Headers params) {
        return PUT(new RequestParams(params));
    }
    */
    public RestResponse DELETE() {
        return RestRequest.doRequest(DELETE, spec);
    }
    /*
    public RestResponse DELETE(RequestParams params) {
        return DeleteRequest(url, params);
    }
    public RestResponse DELETE(String... params) {
        return DELETE(new RequestParams(params));
    }
    public RestResponse DELETE(UrlParams params) {
        return DELETE(new RequestParams(params));
    }
    public RestResponse DELETE(Headers params) {
        return DELETE(new RequestParams(params));
    }
    */
    public boolean isAlive() {
        return isAlive(2000);
    }
    public boolean isAlive(int liveTimeMSec) {
        long start = currentTimeMillis();
        ResponseStatusType status;
        do { status = GET().status().type();
        } while (status != OK && currentTimeMillis() - start < liveTimeMSec);
        return status == OK;
    }
}
