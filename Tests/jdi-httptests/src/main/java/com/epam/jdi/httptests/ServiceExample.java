package com.epam.jdi.httptests;

import com.epam.jdi.http.annotations.*;
import com.epam.jdi.http.requests.RestMethod;

import static com.jayway.restassured.http.ContentType.JSON;

/**
 * Created by Roman_Iovlev on 9/25/2016.
 */
@ServiceDomain("http://httpbin.org/")
public class ServiceExample {
    @ContentType(JSON) @GET("/get")
    @Headers({
        @Header(name = "Name", value = "Roman"),
        @Header(name = "Id", value = "Test")
    })
    static RestMethod getMethod;

    @Header(name = "Type", value = "Test")
    @POST("/post")
    RestMethod postMethod;

    @PUT("/put") RestMethod putMethod;
    @PATCH("/patch") RestMethod patch;
    @DELETE("/delete") RestMethod delete;
    @GET("/status/%s") RestMethod status;

}
