import {gql} from '@apollo/client'

const CREATE_EXPENSE = gql`
    mutation SaveExpense($input: ExpenseInput!) {
        saveExpense(expense: $input) {
            id
            amount
            category
            date
            description
            location
            paymentMethod
            transactionType
        }
    }
`;

const UPDATE_EXPENSE = gql`
    mutation UpdateExpense($input: ExpenseUpdateInput!) {
        updateExpense(expense: $input) {
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