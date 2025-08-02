import { gql } from '@apollo/client';

const FETCH_USERS = gql`
  query Users($pageNumber: Int!, $pageSize: Int!, $sortBy: UserSortField, $sortDirection: SortDirection) {
    users(pageNumber: $pageNumber, pageSize: $pageSize, sortBy: $sortBy, sortDirection: $sortDirection) {
      total
      data{
        createdAt
            createdBy
            email
            firstName
            gendar
            id
            lastName
            phoneNumber
            tenantId
            updatedAt
            roles {
              roleName
            }
      }
    }
  }
`;

const FETCH_USER_BY_ID = gql`
  query User($id: ID!) {
    user(id: $id) {
      id
      firstName
      lastName
      email
      gendar
      phoneNumber
      roles {
        roleName
      }
    }
  }
`;

export { FETCH_USERS, FETCH_USER_BY_ID };
