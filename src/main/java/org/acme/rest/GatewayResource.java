package org.acme.rest;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Route;
import org.apache.camel.processor.loadbalancer.StickyLoadBalancer;
import org.apache.commons.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/")
public class GatewayResource {

    @Inject
    CamelContext camelContext;

    @Inject
    ProducerTemplate producerTemplate;

    @GET
    @Path("/db/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        
        String ret = producerTemplate.requestBody("direct:getUser", null, String.class);
       
        Response.ResponseBuilder builder;
        builder = Response.status(200).entity(ret);
        return builder.build();
    }

    @POST
    @Path("/db/users")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUsers(@Body String body) {
        
        producerTemplate.requestBody("direct:insertUser", body, String.class);
        Response.ResponseBuilder builder;
        builder = Response.status(201);
        return builder.build();
    }

    @POST
    @Path("queue/{queueName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMessage(@PathParam("queueName") String queueName, String body)
    {
        producerTemplate.sendBodyAndHeader("direct:sendJMS", body, "QUEUE", queueName);
        Response.ResponseBuilder builder;
        builder = Response.status(201);
        return builder.build();
    }

    @GET
    @Path("/queue/{queueName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pollMessaeg(@PathParam("queueName") String queueName) {
        
        String ret = producerTemplate.requestBodyAndHeader("direct:receiveJMS", null, "QUEUE", queueName, String.class);
       
        Response.ResponseBuilder builder;
        if (ret.equals("null"))
        {
            builder = Response.status(204);
        }
        else
        {
            builder = Response.status(200).entity(ret);
        }
        return builder.build();
    }

}