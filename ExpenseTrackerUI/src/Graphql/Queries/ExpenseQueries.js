import { gql } from '@apollo/client';

const FETCH_EXPENSES = gql`
  query FetchExpenses {
    fetchExpenses {
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

const FETCH_EXPENSE_BY_ID = gql`
  query FetchExpense($id: ID!) {
    fetchExpense(id: $id) {
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

export { FETCH_EXPENSES, FETCH_EXPENSE_BY_ID };
