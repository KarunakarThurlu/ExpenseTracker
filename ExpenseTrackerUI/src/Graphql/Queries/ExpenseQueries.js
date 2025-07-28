import { gql } from '@apollo/client';

const FETCH_EXPENSES = gql`
  query Expenses($pageNumber: Int!, $pageSize: Int!, $sortBy: UserSortField, $sortDirection: SortDirection) {
    expenses(pageNumber: $pageNumber, pageSize: $pageSize, sortBy: $sortBy, sortDirection: $sortDirection) {
      total
      data {
        amount
        category
        createdAt
        date
        description
        id
        location
        paymentMethod
        transactionType
        updatedAt
      }
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
