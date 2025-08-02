import { gql } from '@apollo/client';

const DASHBOARD_QUERY = gql`
  query DashboardData($fromDate: String!, $toDate: String!) {
    dashboardData(fromDate: $fromDate, toDate: $toDate) {
      transactionTypeByAmount { key value }
      groupingByCategory { key value }
      groupingByPaymentMethod { key value }
      lastFiveTransactions {
        amount category createdAt date paymentMethod transactionType
      }
    }
  }
`;

const CHANGE_PASSWORD = gql`
    mutation UpdatePassword($input : UpdatePasswordInput!){
     updatePassword(updatePassword: $input)
    }
`;

export {DASHBOARD_QUERY,CHANGE_PASSWORD};