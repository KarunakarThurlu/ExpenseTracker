package com.expensetracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;

@Controller
public class GraphQLController {
	
    @Autowired
    private GraphQLSchema graphQLSchema;

    @GetMapping("/schema")
    public ResponseEntity<String> getSchema() {
    	SchemaPrinter schemaPrinter = new SchemaPrinter();
    	String schemaString = schemaPrinter.print(graphQLSchema);
    	return ResponseEntity.ok(schemaString);
    }
}
