import React, { useState } from 'react';
import { useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import AccountCircleRoundedIcon from '@mui/icons-material/AccountCircleRounded';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

const apiUrl = import.meta.env.VITE_BACKEND_URL;

const Signup = () => {
    const theme = useTheme();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        email: '',
        password: '',
        confirmPassword: ''
    });
    const [error, setError] = useState({
        email: '',
        password: '',
        confirmPassword: ''
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

        if (validateForm()) {
            axios.post(`${apiUrl}/signup`, formData)
                .then(response => {
                    if (response.status === 201 && response.data.AuthToken) {
                        localStorage.setItem('token', response.data.AuthToken);
                        setFormData({
                            email: '',
                            password: '',
                            confirmPassword: ''
                        });
                        navigate('/signin');
                    } else {
                        setError({ ...error, general: response.data.message });
                    }
                })
                .catch(() => {
                    setError({ ...error, general: 'An error occurred. Please try again.' });
                });
        }
    };

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
        if (name === 'email') {
            if (value === '') {
                return 'This field is required';
            }
            if (!/^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$/.test(value)) {
                return 'Email is not valid';
            }
        }
        if (name === 'password') {
            if (value === '') {
                return 'This field is required';
            }
            if (!/(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W)/.test(value)) {
                return 'Password should contain at least one uppercase, one lowercase, one number and one special character';
            }
        }
        if (name === 'confirmPassword') {
            if (value === '') {
                return 'This field is required';
            }
            if (value !== formData.password) {
                return 'Passwords do not match';
            }
        }
        return '';
    }

    return (
        <Box
            component="form"
            noValidate
            autoComplete="off"
            sx={{
                width: '100%',
                maxWidth: '400px',
                margin: '0 auto',
                padding: theme.spacing(2.5),
                boxShadow: '0 0 15px rgba(0, 0, 0, 0.1)',
                borderRadius: '10px',
                backgroundColor: '#fff',
                marginTop: '50px',
                textAlign: 'center'
            }}
        >
            <AccountCircleRoundedIcon />
            <Stack spacing={1.4}>
                <TextField
                    name="email"
                    label="Email"
                    variant="outlined"
                    margin="normal"
                    value={formData.email}
                    onChange={handleChange}
                    error={error.email !== ''}
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
                    error={error.password !== ''}
                    helperText={error.password}
                />
                <TextField
                    name="confirmPassword"
                    label="Confirm Password"
                    variant="outlined"
                    margin="normal"
                    type="password"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    error={error.confirmPassword !== ''}
                    helperText={error.confirmPassword}
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="#fbc600"
                    onClick={handleSubmit}
                    sx={{ marginTop: theme.spacing(0.5), backgroundColor: "#fbc600" }}
                >
                    Signup
                </Button>
                <div>
                    <Link to="/signup" style={{ textDecoration: 'none', marginRight: '10px', color: "#3b3d49" }}>
                        Create an account
                    </Link>
                    <Link to="/signin" style={{ textDecoration: 'none', color: "#3b3d49" }}>
                        Signin
                    </Link>
                </div>
            </Stack>
        </Box>
    )
}
export default Signup;