
import React, { useState } from 'react';
import { useTheme } from '@mui/material/styles';
import LockPersonRoundedIcon from '@mui/icons-material/LockPersonRounded';
import { Link, useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import { Button, Stack, TextField } from '@mui/material';
import axios from 'axios';
import {useNotifier} from '../Utils/Notifyer'

const apiUrl = import.meta.env.VITE_BACKEND_URL;

const Signin = () => {
    const theme = useTheme();
    const { showNotification } =   useNotifier();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });
    const [error, setError] = useState({
        email: '',
        password: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
        setError({
            ...error,
            [name]: checkConstraints(name, value)
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
         
        //Signin API Call and forward /dashboard if success
        if (validateForm()) {
            axios.post(`${apiUrl}/signin`, formData)
                .then(response => {
                    if (response.status === 202 && response.data.AuthToken) {
                        const token = response.data.AuthToken;
                         const payload = JSON.parse(atob(token.split('.')[1]));
                        localStorage.setItem('token', token);
                        localStorage.setItem('userId', payload.userId);
                        const roles = payload.roles.map(role=>role.authority).join(',');
                        localStorage.setItem('roles', roles);
                        setFormData({
                            email: '',
                            password: ''
                        });
                        navigate('/dashboard');
                    } else {
                        setError({ ...error, general: response.data.message });
                    }
                    showNotification('Login is success','success')
                }).catch(error => {
                      const message =  error?.response.data.message || "Internal Server Error"
                    showNotification(message,'error')
                    setError({ ...error, general: 'An error occurred. Please try again.' });
                });
        }
    }

    const validateForm = () => {
        let valid = true;
        let errorObj = { ...error };
        for (let key in formData) {
            const errorMessage = checkConstraints(key, formData[key]);
            errorObj[key] = errorMessage;
            if (errorMessage !== '') {
                valid = false;
            }
        }
        setError(errorObj);
        return valid;
    }

    const checkConstraints = (name, value) => {
        switch (name) {
            case 'email':
                if (value === '') {
                    return 'Email is required';
                } else {
                    return '';
                }
            case 'password':
                if (value === '') {
                    return 'Password is required';
                } else {
                    return '';
                }
            default:
                return '';
        }
    }

    return (
        <Box
            component="form"
            noValidate
            sx={{
                width: '100%',
                maxWidth: '400px',
                margin: '0 auto',
                padding: theme.spacing(2.5),
                boxShadow: '0 0 15px rgba(0, 0, 0, 0.1)',
                borderRadius: '10px',
                backgroundColor: '#fff',
                marginTop: '50px',
                textAlign:'center'
            }}
        >
            <LockPersonRoundedIcon />
            <Stack spacing={1.4}>
                <TextField
                    name="email"
                    label="Email"
                    variant="outlined"
                    margin="normal"
                    value={formData.email}
                    onChange={handleChange}
                    error={error.email !== '' ? true : false}
                    helperText={error.email}
                />
                <TextField
                    name="password"
                    label="Password"
                    variant="outlined"
                    margin="normal"
                    type="password"
                    value={formData.password}
                    onChange={handleChange}
                    error={error.password !== '' ? true : false}
                    helperText={error.password}
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="#fbc600"
                    onClick={handleSubmit}
                    sx={{ marginTop: theme.spacing(0.5), backgroundColor: "#fbc600" }}
                >
                    SingnIn
                </Button>

                <div>
                    <Link to="/signup" style={{ textDecoration: 'none', marginRight: '10px',color: "#3b3d49" }}>
                        Create an account
                    </Link>

                    <Link to="/signin" style={{ textDecoration: 'none',color: "#3b3d49" }}>
                        Signin
                    </Link>
                </div>


            </Stack >
        </Box>
    )
}
export default Signin;