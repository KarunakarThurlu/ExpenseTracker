import React, { useState, useEffect } from 'react';
import { Box, Button, Typography } from '@mui/material';
import DataTable from '../../Utils/DataTable';
import { FETCH_TENANTS } from '../../Graphql/Queries/TenantQueries';
import { useApolloClient } from "@apollo/client";
import AddTenant from './AddTenant';
import { useNotifier } from '../../Utils/Notifyer';
import { ADD_TENANT, UPDATE_TENANT } from '../../Graphql/Mutations/TenantMutations';

const Tenants = () => {
    const { showNotification } =   useNotifier();
    const client = useApolloClient();
    const [openTenantModal, setOpenTenantModal] = useState(false);
    const [tenantData, setTenantData] = useState({});

    const fetchTenants = async ({ pageSize, pageNumber, sortBy, sortDirection }) => {
        const { data } = await client.query({
            query: FETCH_TENANTS,
            variables: { pageSize, pageNumber, sortBy, sortDirection },
            fetchPolicy: 'network-only',
        });
        return {
            data: data.tenants.data,
            total: data.tenants.total,
        };
    };
    const handleEditOrAddTenant = (data) => {
        setTenantData(data);
        setOpenTenantModal(true);
    };

    const columns = [
        { field: 'id', headerName: 'ID', flex: 1 },
        { field: 'name', headerName: 'Name', flex: 1 },
        { field: 'createdAt', headerName: 'Created At', flex: 1 },
        { field: 'updatedAt', headerName: 'Updated At', flex: 1 },
        { field: 'licenseExpiry', headerName: 'License Expiry', flex: 1 },
        { field: 'maxUsersAllowed', headerName: 'Max Users Allowed', flex: 1 },
        {
            field: 'actions',
            headerName: 'Actions',
            renderCell: (params) => (
                <Button
                    variant="outlined"
                    color="warning"
                    size="small"
                    onClick={() => {
                        setTenantData(params.row);
                        setOpenTenantModal(true);
                    }}
                >
                    Edit
                </Button>
            )
        }
    ];

    const handleSave = async (tenant) => {
        console.log("Tenant saved:", tenant);
        try {
            const { data } = await client.mutate({
                mutation: tenant.id ? UPDATE_TENANT : ADD_TENANT,
                variables: {
                    input: {
                        id: tenant.id || null,
                        name: tenant.name,
                        licenseExpiry: tenant.licenseExpiry,
                        maxUsersAllowed: parseInt(tenant.maxUsersAllowed, 10) || 0,
                    },
                },
            });
            if (tenant.id) {
                console.log("Tenant updated successfully:", data.updateTenant);
                showNotification("Tenant Updated Successfully!", 'success');
            } else {
                console.log("Tenant added successfully:", data.addTenant);
                showNotification("Tenant Added Successfully!", 'success');
            }
        } catch (error) {
            showNotification(error.message, 'error');
            console.log("Error saving tenant:", error);
        }

        setOpenTenantModal(false);
    };

    return (
        <>
            <AddTenant open={openTenantModal} initialData={tenantData} onClose={() => setOpenTenantModal(false)} onSave={handleSave} />
            <Box display="flex" justifyContent="space-between" alignItems="center" sx={{margin:'5px'}}>
                <Typography variant='h5'>Tenants</Typography>
                <Box display="flex" alignItems="center">
                    <Box>
                        <Button variant="outlined" onClick={() => handleEditOrAddTenant({})} color="warning" size="small" sx={{ mr: 1 }}>
                            Add Tenant
                        </Button>
                    </Box>
                </Box>
            </Box>
            <DataTable columns={columns} fetchData={fetchTenants} />
        </>
    );
}

export default Tenants;