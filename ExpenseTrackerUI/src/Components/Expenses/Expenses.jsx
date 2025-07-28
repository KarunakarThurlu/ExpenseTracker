import { Box, Button, Typography } from "@mui/material";
import DataTable from "../../Utils/DataTable"
import AddExpense from "./AddExpense";
import { useState } from "react";
import { useMutation } from "@apollo/client";
import { CREATE_EXPENSE, UPDATE_EXPENSE } from "../../Graphql/Mutations/ExpenseMutations";
import { FETCH_EXPENSES, FETCH_EXPENSE_BY_ID } from "../../Graphql/Queries/ExpenseQueries";
import { useApolloClient } from "@apollo/client"; // Add this import at the top if not present
import dayjs from "dayjs";

import { enumValueMap } from "../../Utils/EnumValueMap"; // Ensure this import is correct
import { useNotifier } from "../../Utils/Notifyer";
import { idID } from "@mui/material/locale";

const Expenses = () => {
     const { showNotification } =   useNotifier();
    const client = useApolloClient();
    const [openExpenseModal, setOpenExpenseModal] = useState(false);
    const [expenseData, setExpenseData] = useState({});

    const handleExpenseChange = (data) => {
        setExpenseData(data);
    };
    const handleAddExpense = () => {
        setExpenseData({});
        setOpenExpenseModal(true);
    };
    const handleSaveExpense = async (expenseData) => {
        expenseData.userId = localStorage.getItem('userId') || '1';
        try {
            const { data } = await client.mutate({
                mutation: expenseData.id ? UPDATE_EXPENSE : CREATE_EXPENSE,
                variables: {
                    input: {
                        id: expenseData.id || null, // Use id if available, otherwise null
                        amount: parseFloat(expenseData.amount), // 👈 this is key
                        category: enumValueMap.Category[expenseData.category],
                        date: dayjs(expenseData.date).toISOString(),
                        description: expenseData.description,
                        location: expenseData.location,
                        paymentMethod: enumValueMap.PaymentMethod[expenseData.paymentMethod],
                        transactionType: enumValueMap.TransactionType[expenseData.transactionType],
                        userId: expenseData.userId,
                    }
                }
            });
            if (expenseData.id) {
                console.log("Expense updated successfully:", data.updateExpense);
                showNotification("Expense Updated Successfully!",'success')
            } else {
                console.log("Expense created successfully:", data.saveExpense);
                showNotification("Expense Added Successfully!",'success')
            }
            setOpenExpenseModal(false);
        } catch (error) {
            console.log("Error creating expense:", error);
            showNotification(error.message,'error')
        }
    };


    const fetchExpenses = async ({ pageSize, pageNumber, sortBy, sortDirection }) => {
        const { data } = await client.query({
            query: FETCH_EXPENSES,
            variables: { pageSize, pageNumber, sortBy, sortDirection },
            fetchPolicy: 'network-only',
        });
        return {
            data: data.expenses.data,
            total: data.expenses.total,
        };
    };
    const columns = [
        { field: 'id', headerName: 'ID',flex: 1  },
        { field: 'description', headerName: 'Description', flex: 1 },
        { field: 'amount', headerName: 'Amount', width: 120, type: 'number', flex: 1 },
        {
            field: 'date',
            headerName: 'Date',
            flex: 1 
        },
        { field: 'category', headerName: 'Category', flex: 1  },
        { field: 'location', headerName: 'Location', flex: 1 },
        { field: 'paymentMethod', headerName: 'Payment Method',flex: 1  },
        { field: 'transactionType', headerName: 'Transaction Type',flex: 1 },
        {
            field: 'createdAt',
            headerName: 'Created At',
           flex: 1 ,
            type: 'dateTime',
            valueGetter: (params) => params.value ? new Date(params.value) : null,
        },
        {
            field: 'updatedAt', headerName: 'Updated At',flex: 1 
        },
        // Edit column with pencil icon
        {
            field: 'actions',
            headerName: 'Actions',
            flex: 1,
            renderCell: (params) => (
                <Button
                    variant="outlined"
                    color="warning"
                    size="small"
                    onClick={() => {
                        setExpenseData(params.row);
                        setOpenExpenseModal(true);
                    }}
                >
                    Edit
                </Button>
            )
        }
    ];


    return (
        <>
            <AddExpense open={openExpenseModal} initialData={expenseData} onSave={handleSaveExpense} onClose={() => setOpenExpenseModal(false)} />
            <Box display="flex" justifyContent="space-between" alignItems="center" sx={{margin:'5px'}}>
                <Typography variant='h5'>Expenses</Typography>
                <Box display="flex" alignItems="center">
                    <Box>
                        <Button variant="outlined" onClick={() => setOpenExpenseModal(true)} color="warning" size="small" sx={{ mr: 1 }}>
                            Add Expense
                        </Button>
                    </Box>
                </Box>
            </Box>
            <DataTable columns={columns} fetchData={fetchExpenses} />
        </>
    )
}

export default Expenses;