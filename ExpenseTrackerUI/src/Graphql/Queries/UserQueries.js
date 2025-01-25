import { gql } from '@apollo/client';

const FETCH_USERS = gql`
  query FetchUsers {
    fetchUsers {
      id
      firstName
      lastName
      email
      phoneNumber
      roles {
        roleName
      }
      expenses {
        description
        amount
        category
      }
    }
  }
`;

const FETCH_USER_BY_ID = gql`
  query FetchUser($id: ID!) {
    fetchUser(id: $id) {
      id
      firstName
      lastName
      email
      phoneNumber
      roles {
        roleName
      }
      expenses {
        description
        amount
        category
      }
    }
  }
`;

export { FETCH_USERS, FETCH_USER_BY_ID };
