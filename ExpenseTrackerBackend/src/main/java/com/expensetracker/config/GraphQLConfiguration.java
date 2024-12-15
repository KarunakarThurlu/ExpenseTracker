package com.expensetracker.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Configuration
public class GraphQLConfiguration {

    @Bean
    public GraphQLSchema graphQLSchema() throws IOException {
        // Read the GraphQL schema from a file (e.g., schema.graphql)
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(new ClassPathResource("graphql/expense-tracker.graphqls").getInputStream());

        // Create RuntimeWiring to connect resolvers to schema
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                // ... add resolvers for your queries and mutations here
                .build();

        // Generate the GraphQL schema
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }
}
