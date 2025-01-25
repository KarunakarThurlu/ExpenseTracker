import {gql} from '@appolo/client';

const CREATE_EXPENSE = gql`
    mutation CreateExpense($input: CreateExpenseInput!) {
        createExpense(input: $input) {
            id
            description
            amount
            date
            location
            category
            paymentMethod
        }
    }
`;

const UPDATE_EXPENSE = gql`
    mutation UpdateExpense($id: ID!, $input: UpdateExpenseInput!) {
        updateExpense(id: $id, input: $input) {
            id
            description
            amount
            date
            location
            category
            paymentMethod
        }
    }
`;

const DELETE_EXPENSE = gql`
    mutation DeleteExpense($id: ID!) {
        deleteExpense(id: $id) {
            id
            description
            amount
            date
            location
            category
            paymentMethod
        }
    }
`;

export {CREATE_EXPENSE, UPDATE_EXPENSE, DELETE_EXPENSE};