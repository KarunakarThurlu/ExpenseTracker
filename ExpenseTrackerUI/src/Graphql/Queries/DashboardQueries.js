import { gql } from '@apollo/client';

export const DASHBOARD_QUERY = gql`
  query DashboardData($fromDate: String!, $toDate: String!) {
    dashboardData(fromDate: $fromDate, toDate: $toDate) {
      transactionTypeByAmount {
        key
        value
      }
      groupingByCategory {
        key
        value
      }
      groupingByPaymentMethod {
        key
        value
      }
      lastFiveTransactions {
        amount
        category
        createdAt
        date
        paymentMethod
        transactionType
      }
    }
  }
`;
