import React, { useState, useEffect } from 'react';
import { Box, Button, Typography } from '@mui/material';
import DataTable from '../../Utils/DataTable';
import { FETCH_USERS } from '../../Graphql/Queries/UserQueries';
import { useApolloClient } from '@apollo/client';
import AddUser from './AddUser';
import {CREATE_USER, UPDATE_USER } from '../../Graphql/Mutations/UserMutations';
import dayjs from 'dayjs';
import { useNotifier } from '../../Utils/Notifyer';

const Users = () => {
    const { showNotification } =   useNotifier();
    const client = useApolloClient();
    const [openUserModal, setOpenUserModal] = useState(false);
    const [userData, setUserData] = useState({});
    const fetchUsers = async ({ pageSize, pageNumber, sortBy, sortDirection }) => {
        const { data } = await client.query({
            query: FETCH_USERS,
            variables: { pageSize, pageNumber, sortBy, sortDirection },
            fetchPolicy: 'network-only',
        });
        return {
            data: data.users.data || [],
            total: data.users.total || 0,
        };
    };

const columns = [
    { field: 'id', headerName: 'ID', flex: 1 },
    { field: 'firstName', headerName: 'First Name', flex: 1 },
    { field: 'lastName', headerName: 'Last Name', flex: 1 },
    { field: 'email', headerName: 'Email', flex: 1 },
    { field: 'phoneNumber', headerName: 'Phone Number', flex: 1 },
    { field: 'gendar', headerName: 'Gender', flex: 1 },
    { field: 'tenantId', headerName: 'Tenant ID', flex: 1 },
    {
        field: 'createdAt',
        headerName: 'Created At',
        flex: 1,

    },
    {
        field: 'updatedAt',
        headerName: 'Updated At',
        flex: 1,

    },
    //Edit column with pencil icon
    {
        field: 'actions',
        headerName: 'Actions',
        flex: 1,
        renderCell: (params) => (
            <Button
                variant="outlined"
                color="primary"
                size="small"
                onClick={() => {
                    //open edit modal or handle edit action
                    setOpenUserModal(true);
                    // You can pass the user data to the modal if needed
                    setUserData(params.row);
                    console.log("Edit user:", params.row);
                }}
            >
                Edit
            </Button>
        ),
    },
];  

    const handleSave = async (user) => {
        console.log("User saved:", user);
        // Make GraphQL mutation to save user, like save Expense
        try {
            const { data } = await client.mutate({
                mutation: user.id ? UPDATE_USER: CREATE_USER,
                variables: { input : user }
            });
            if(user.id){
                showNotification('User Updated Successfully!','success')
            }else{
                showNotification('User Created Successfully!','success')
            }
            console.log("User added:", data);
             setOpenUserModal(false);
        } catch (error) {
            console.log("Error adding user:", error);
            showNotification(error.message,'error')
        }
        setOpenUserModal(false);
    };

    const handleEditOrAddUser = (user) => {
        setUserData(user);
        setOpenUserModal(true);
    }

    return (
        <>
            <AddUser open={openUserModal} initialData={userData}  onSave={handleSave} onClose={() => setOpenUserModal(false)} />
            <Box display="flex" justifyContent="space-between" alignItems="center" sx={{margin:'5px'}}>
                <Typography variant='h5'>Users</Typography>
                <Box display="flex" alignItems="center">
                    <Box>
                        <Button variant="outlined" onClick={() => handleEditOrAddUser({})} color="warning" size="small" sx={{ mr: 1 }}>
                            Add User
                        </Button>
                    </Box>
                </Box>
            </Box>
            <DataTable columns={columns} fetchData={fetchUsers} />
        </>
    );
};

export default Users;