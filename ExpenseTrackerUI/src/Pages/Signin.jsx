
//Login page with material-ui
import React, { useState } from 'react';
import { useTheme } from '@mui/material/styles';
import LockPersonRoundedIcon from '@mui/icons-material/LockPersonRounded';
import { Link } from 'react-router-dom';
import Box from '@mui/material/Box';
import { Button, Stack, TextField } from '@mui/material';


const Signin = () => {
    const theme = useTheme();

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
        if (validateForm()) {
            console.log('Form Submitted');
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