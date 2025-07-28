import React, { useEffect, useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Box } from '@mui/material';
import { useMutation } from '@apollo/client';
import { ADD_TENANT } from '../../Graphql/Mutations/TenantMutations';

const AddTenant = ({ open, onClose, onSave, initialData = {} }) => {

    const [form, setForm] = useState({
        id: initialData.id || '',
        name: initialData.name || '',
        licenseExpiry: initialData.licenseExpiry || '',
        maxUsersAllowed: initialData.maxUsersAllowed || '',
    });
    const [errors, setErrors] = useState({
        name: '',
        licenseExpiry: '',
        maxUsersAllowed: ''
    });


    const handleSubmit = (e) => {
        e.preventDefault();
        if(validateForm()){
            onSave(form);
        }
    };

    const validateForm = () => {
        const newErrors = {}
        if (!form.name) newErrors.name = 'Please Enter Tenant Name'
        if (!form.licenseExpiry) newErrors.licenseExpiry = 'Please Select License Expiry'
        if (!form.maxUsersAllowed) newErrors.maxUsersAllowed = 'Please Enter Max Users Allowed'
       
        setErrors(newErrors);
        const len=Object.keys(newErrors).length
        return  len=== 0;
    }
    const handleChange = (e) => {
        setForm((prev) => ({
            ...prev,
            [e.target.name]: e.target.value,
        }));
    };

    useEffect(() => {
        setForm({
            id: initialData.id || 0,
            name: initialData.name || '',
            licenseExpiry: initialData.licenseExpiry || '',
            maxUsersAllowed: initialData.maxUsersAllowed || '',
        });
    }, [initialData]);

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
            <DialogTitle>Add Tenant</DialogTitle>
            <DialogContent>
                <form onSubmit={handleSubmit}>
                    <Box display="flex" flexDirection="column" gap={2} mt={1}>
                        <TextField
                            label="Name"
                            name="name"
                            value={form.name}
                            onChange={handleChange}
                            error={!!(errors && errors.name && errors.name.length)}
                            helperText={errors && errors.name && errors.name.length ? errors.name : ''}
                            fullWidth
                        />
                        <TextField
                            label="License Expiry"
                            name="licenseExpiry"
                            type="date"
                            value={form.licenseExpiry}
                            onChange={handleChange}
                            slotProps={{ inputLabel: { shrink: true } }}
                            error={!!(errors && errors.licenseExpiry)}
                            helperText={errors && errors.licenseExpiry ? errors.licenseExpiry : ''}
                            fullWidth
                        />
                        <TextField
                            label="Max Users Allowed"
                            name="maxUsersAllowed"
                            type="number"
                            value={form.maxUsersAllowed}
                            onChange={handleChange}
                            error={!!(errors && errors.maxUsersAllowed)}
                            helperText={errors && errors.maxUsersAllowed ? errors.maxUsersAllowed : ''}
                            fullWidth
                        />
                    </Box>
                    <DialogActions sx={{ mt: 2 }}>
                        <Button onClick={handleSubmit} variant='outlined'>
                            Submit
                        </Button>
                        <Button  onClick={onClose} variant='outlined'>
                            Cancel
                        </Button>
                    </DialogActions>
                </form>
            </DialogContent>
        </Dialog>
    );
};

export default AddTenant;
