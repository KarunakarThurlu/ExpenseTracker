import { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Box } from '@mui/material';
import { CHANGE_PASSWORD } from '../../Graphql/Mutations/CommonMutation';
import { useMutation } from '@apollo/client';
import { useNotifier } from '../../Utils/Notifyer';

const ChangePassword = ({ open, onClose, user }) => {

    const { showNotification } = useNotifier();
    const [state, setState] = useState({
        showChangePassword: false
    });
    const [form, setForm] = useState({
        oldPassword: '',
        confirmPassword: '',
        newPassword: '',
        id: ''
    });
    const [errors, setErrors] = useState({
        oldPassword: '',
        confirmPassword: '',
        newPassword: ''
    });
    const hideChangePassword = () => {
        setState(prevState => ({ ...prevState, showChangePassword: false }))
    }

    const [updatePwd] = useMutation(CHANGE_PASSWORD, {
        onCompleted: (data) => {
            hideChangePassword();
            onClose()
            showNotification(data.updatePassword, 'success');
        },
        onError: (error) => {
            hideChangePassword();
            onClose()
            showNotification(error.message, 'error');
        },
    });
    const handleSavePwd = (changePwd) => {
        console.log(form)
        updatePwd({
            variables: {
                input: {
                    id: user?.id || localStorage.getItem("userId"),
                    oldPassword: form.oldPassword,
                    password: form.confirmPassword,
                },
            },
        });
    }

    const userRoles = localStorage.getItem('roles');

    const validateForm = () => {
        const newErrors = {}
        if (!userRoles.split(",").some(role => role === 'ROLE_SUPER_ADMIN')) {
            if (!form.oldPassword) newErrors.oldPassword = 'Please Enter oldPassword'
        }
        if (!form.confirmPassword) newErrors.confirmPassword = 'Please Confirm Password'
        if (!form.newPassword) newErrors.newPassword = 'Please Enter New Password'
        if (form.confirmPassword !== form.newPassword) {
            newErrors.newPassword = 'Confirm Password And New Password not matched'
        }
        setErrors(newErrors);
        const len = Object.keys(newErrors).length
        return len === 0;
    }
    const handleChange = (e) => {
        setForm((prev) => ({
            ...prev,
            [e.target.name]: e.target.value,
        }));
    };

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
            <DialogTitle>Change Password</DialogTitle>
            <DialogContent>
                <Box display="flex" flexDirection="column" gap={2} mt={1}>
                    {!userRoles.split(",").some(role => role === 'ROLE_SUPER_ADMIN') && (<TextField
                        label="Old Password"
                        name="oldPassword"
                        type='password'
                        value={form.oldPassword}
                        onChange={handleChange}
                        error={!!(errors && errors.oldPassword)}
                        helperText={errors && errors.oldPassword ? errors.oldPassword : ''}
                        fullWidth
                    />)}
                    <TextField
                        label="Confirm Password"
                        name="confirmPassword"
                        type='password'
                        value={form.confirmPassword}
                        onChange={handleChange}
                        error={!!(errors && errors.confirmPassword)}
                        helperText={errors && errors.confirmPassword ? errors.confirmPassword : ''}
                        fullWidth
                    />
                    <TextField
                        label="New Password"
                        name="newPassword"
                        type='password'
                        value={form.newPassword}
                        onChange={handleChange}
                        error={!!(errors && errors.newPassword)}
                        helperText={errors && errors.newPassword ? errors.newPassword : ''}
                        fullWidth
                    />
                </Box>
                <DialogActions sx={{ mt: 2 }}>
                    <Button onClick={handleSavePwd} variant='outlined'>
                        Submit
                    </Button>
                    <Button onClick={onClose} variant='outlined'>
                        Cancel
                    </Button>
                </DialogActions>
            </DialogContent>
        </Dialog>
    );
};

export default ChangePassword;
