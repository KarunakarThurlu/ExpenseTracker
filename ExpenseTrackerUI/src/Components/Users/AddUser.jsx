import {
    Dialog, DialogTitle, DialogContent, DialogActions,
    Button, TextField, Select, MenuItem, OutlinedInput,
    Checkbox, ListItemText, FormControl, InputLabel, Box, Stack,
    Chip
} from '@mui/material';
import { useEffect, useState } from 'react';


const AddUser = ({ open, onClose, onSave, initialData = {} }) => {
    console.log("USER INITIAL DATA ",initialData)
    const [form, setForm] = useState({
        id: '',
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        gendar: '',
        password: '',
        tenantId:0,
        createdBy:0,
        roles: ['USER'],
    });

    const [errors, setErrors] = useState({});

    const userRoles = localStorage.getItem('roles')?.split(',') || [];
    const allRoles = ['USER', 'ADMIN', 'SUPER_ADMIN'];
    const gendar = ['MALE', 'FEMALE'];

    useEffect(() => {
        setForm({
            id: initialData.id || '',
            firstName: initialData.firstName || '',
            lastName: initialData.lastName || '',
            email: initialData.email || '',
            phoneNumber: initialData.phoneNumber || '',
            gendar: initialData.gendar || '',
            tenantId:initialData.tenantId || '',
            createdBy:initialData.createdBy || '',
            roles: initialData?.roles?.map(r => r.roleName) || [],
        });
        setErrors({})
    }, [initialData]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
    };

    const handleRolesChange = (e) => {
        const { value } = e.target;
        setForm(prev => ({
            ...prev,
            roles: typeof value === 'string' ? value.split(',') : value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if(validateForm()){
            const payload = {
            ...form,
            roles: form.roles?.map(role => ({ roleName: role }))
        };
        console.info(payload);
        onSave(payload);  // pass new object
        }

    };
    const validateForm = () => {
        const newErrors = {}
        if (!form.firstName) newErrors.firstName = 'Please Enter FirstName'
        if (!form.lastName) newErrors.lastName = 'Please Enter LastName'
        if (!form.email) newErrors.email = 'Please Enter Email'
        if (!form.phoneNumber) newErrors.phoneNumber = 'Please Enter PhoneNumber'
        if (!form.gendar) newErrors.gendar = 'Please Select Gendar'
        if (!form.firstName) newErrors.firstName = 'Please Enter FirstName'
        if (!form.firstName) newErrors.firstName = 'Please Enter FirstName'
        if (initialData.id === undefined) {
            if (!form.password) newErrors.password = 'Please Enter User Temp Password'
        }
        setErrors(newErrors);
        const len=Object.keys(newErrors).length
        return  len=== 0;
    }
    const handleClose = () =>{
        setErrors({});
        onClose();
    }

    return (
        <Dialog
            open={open}
            onClose={onClose}
            fullWidth
            maxWidth="sm"
        >
            <DialogTitle sx={{ paddingTop: 0 }}>{form.id ? 'Edit User' : 'Add User'}</DialogTitle>
            <DialogContent>
                <input type="hidden" name="id" value={form.id} />
                <Stack spacing={2}>
                    {/* Row 1: First & Last Name */}
                    <Box display="flex" gap={2}>
                        <TextField
                            name="firstName"
                            label="First Name"
                            value={form.firstName}
                            onChange={handleChange}
                            fullWidth
                            error={!!errors.firstName}
                            helperText={errors.firstName}
                        />
                        <TextField
                            name="lastName"
                            label="Last Name"
                            value={form.lastName}
                            onChange={handleChange}
                            fullWidth
                            error={!!errors.lastName}
                            helperText={errors.lastName}
                        />
                    </Box>

                    {/* Row 2: Email & Phone Number */}
                    <Box display="flex" gap={2}>
                        <TextField
                            name="email"
                            label="Email"
                            value={form.email}
                            onChange={handleChange}
                            fullWidth
                            error={!!errors.email}
                            helperText={errors.email}
                        />
                        <TextField
                            name="phoneNumber"
                            label="Phone Number"
                            value={form.phoneNumber}
                            onChange={handleChange}
                            fullWidth
                            error={!!errors.phoneNumber}
                            helperText={errors.phoneNumber}
                        />
                    </Box>

                    {/* Row 3: Gendar & Password */}
                    <Box display="flex" gap={2}>
                        <FormControl fullWidth>
                            <InputLabel id="gendar-label">Gendar</InputLabel>
                            <Select
                                labelId="gendar-label"
                                name='gendar'
                                value={form.gendar}
                                onChange={handleChange}
                                input={<OutlinedInput label="Gendar" />}
                                error={!!errors.gendar}
                                helperText={errors.gendar}
                            >
                                {gendar.map((gendar) => (
                                    <MenuItem key={gendar} value={gendar}>
                                        <ListItemText primary={gendar} />
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Box>

                    {initialData.id === undefined && (<TextField
                        name="password"
                        label="Password"
                        value={form.password}
                        onChange={handleChange}
                        fullWidth
                         error={!!errors.password}
                            helperText={errors.password}
                    />)}

                    {/* Row 4: Roles */}
                    {userRoles.includes('ROLE_SUPER_ADMIN') && (
                        <FormControl fullWidth>
                            <InputLabel id="roles-label">Roles</InputLabel>
                            <Select
                                labelId="roles-label"
                                multiple
                                value={form.roles}
                                onChange={handleRolesChange}
                                input={<OutlinedInput label="Roles" />}
                                renderValue={(selected) => (
                                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                                        {selected.map((value) => (
                                            <Chip key={value} label={value} />
                                        ))}
                                    </Box>
                                )}
                            >
                                {allRoles.map((role) => (
                                    <MenuItem key={role} value={role}>
                                        <Checkbox checked={form.roles.includes(role)} />
                                        <ListItemText primary={role} />
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    )}
                </Stack>
            </DialogContent>

            <DialogActions>
                <Button onClick={handleSubmit} variant="outlined">Submit</Button>
                <Button onClick={handleClose} variant="outlined">Cancel</Button>
            </DialogActions>
        </Dialog>
    );
};

export default AddUser;
