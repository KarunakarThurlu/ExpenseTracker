import { gql } from "@apollo/client";

const FETCH_TENANTS = gql`
  query Tenants($pageNumber: Int!, $pageSize: Int!, $sortBy: TenantSortField, $sortDirection: SortDirection) {
    tenants(pageNumber: $pageNumber, pageSize: $pageSize, sortBy: $sortBy, sortDirection: $sortDirection) {
      total
      data {
            createdAt
            id
            licenseExpiry
            maxUsersAllowed
            name
            updatedAt
      }
    }
  }
`;

const FETCH_TENANT_BY_ID = gql`
  query Tenant($id: ID!) {
    tenant(id: $id) {
      id
      name
      email
      phoneNumber
      createdAt
      updatedAt
    }
  }
`;

export { FETCH_TENANTS, FETCH_TENANT_BY_ID };