package com.expensetracker.exceptionhandler;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import com.expensetracker.util.CommonConstants;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@Component
public class GlobalExceptionHandler extends DataFetcherExceptionResolverAdapter {
	
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex,DataFetchingEnvironment env) {
        if (ex instanceof CustomGraphQLException) {
        	CustomGraphQLException customGraphQLException=(CustomGraphQLException) ex;
            return  GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(ex.getMessage())
                    .extensions(Map.of(CommonConstants.RESPONSE_MESSAGE,ex.getMessage(),
                    					CommonConstants.RESPONSE_STATUS,customGraphQLException.getStatusCode()
                    		           ))
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
		} else if (ex instanceof ConstraintViolationException) {
			ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex;
			Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
			return GraphqlErrorBuilder.newError()
									  .errorType(ErrorType.BAD_REQUEST)
									  .message(ex.getMessage())
									  .extensions(Map.of(CommonConstants.RESPONSE_MESSAGE,violations.stream().map(violation -> violation.getMessage()).collect(Collectors.joining(", ")),
											  		     CommonConstants.RESPONSE_STATUS, 400))
					                  .path(env.getExecutionStepInfo().getPath()).build();
		}

        // Default fallback for unexpected errors
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message("Internal Server error : "+ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }
}
