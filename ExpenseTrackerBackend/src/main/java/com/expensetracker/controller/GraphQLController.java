package com.expensetracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;

@Controller
public class GraphQLController {
	
    private GraphQLSchema graphQLSchema;
    public GraphQLController(GraphQLSchema graphQLSchema) {
    	this.graphQLSchema=graphQLSchema;
    }
    
    //It can be used to access GraphQL schema, for example in Postman
    @GetMapping("/schema")
    public ResponseEntity<String> getSchema() {
    	SchemaPrinter schemaPrinter = new SchemaPrinter();
    	String schemaString = schemaPrinter.print(graphQLSchema);
    	return ResponseEntity.ok(schemaString);
    }
}
