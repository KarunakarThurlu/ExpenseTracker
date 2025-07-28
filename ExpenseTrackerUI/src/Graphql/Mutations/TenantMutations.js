import { gql } from "@apollo/client";

export const ADD_TENANT = gql`
    mutation SaveTenant($input: TenantInput!) {
        saveTenant(tenant: $input) {
            id
            name
            licenseExpiry
            maxUsersAllowed
        }
    }
`;
export const UPDATE_TENANT = gql`
    mutation UpdateTenant($input: TenantUpdateInput!) {
        updateTenant(tenant: $input) {
            id
            name
            licenseExpiry
            maxUsersAllowed
        }
    }
`;  
export const DELETE_TENANT = gql`
    mutation DeleteTenant($id: ID!) {
        deleteTenant(id: $id) {
            id
            name
            licenseExpiry
            maxUsersAllowed
        }
    }
`;