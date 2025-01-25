import { gql } from '@apollo/client';

const CREATE_USER = gql`
    mutation CreateUser($input: CreateUserInput!) {
        createUser(input: $input) {
        id
        firstName
        lastName
        email
        phoneNumber
        roles {
            roleName
        }
        }
    }
    `;
const UPDATE_USER = gql`
    mutation UpdateUser($id: ID!, $input: UpdateUserInput!) {
        updateUser(id: $id, input: $input) {
        id
        firstName
        lastName
        email
        phoneNumber
        roles {
            roleName
        }
        }
    }
    `;
const DELETE_USER = gql`
    mutation DeleteUser($id: ID!) {
        deleteUser(id: $id) {
        id
        firstName
        lastName
        email
        phoneNumber
        roles {
            roleName
        }
        }
    }
    `;
const ADD_ROLE_TO_USER = gql`
    mutation AddRoleToUser($userId: ID!, $roleId: ID!) {
        addRoleToUser(userId: $userId, roleId: $roleId) {
        id
        firstName
        lastName
        email
        phoneNumber
        roles {
            roleName
        }
        }
    }
    `;
const REMOVE_ROLE_FROM_USER = gql`
    mutation RemoveRoleFromUser($userId: ID!, $roleId: ID!) {
        removeRoleFromUser(userId: $userId, roleId: $roleId) {
        id
        firstName
        lastName
        email
        phoneNumber
        roles {
            roleName
        }
        }
    }
    `;
export { CREATE_USER, UPDATE_USER, DELETE_USER, ADD_ROLE_TO_USER, REMOVE_ROLE_FROM_USER };