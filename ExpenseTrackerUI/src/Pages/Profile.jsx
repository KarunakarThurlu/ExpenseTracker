import {
  Box,
  Avatar,
  Typography,
  Paper,
  Chip
} from '@mui/material';
import PersonIcon from '@mui/icons-material/Person';
import { useLazyQuery } from '@apollo/client';
import { FETCH_USER_BY_ID } from '../Graphql/Queries/UserQueries';
import { useEffect, useState } from 'react';

const Profile = () => {
  const [state, setSate] = useState({
    avatar_url: '', 
    firstName: '',
    lastName: '',
    email: '',
    gender: '',
    roles: [{roleName:'USER'}],
    phoneNumber: '',
  });
  const [fetchUser] = useLazyQuery(FETCH_USER_BY_ID, {
    onCompleted: (data) => {
      console.log(data)
      setSate(data.user)
    },
    onError: (error) => {
      console.log(error)
    }

  });
  useEffect(()=>{
    getUserData();
  }, [])
  const getUserData = () => {
    const userID = localStorage.getItem("userId")
    fetchUser({
      variables:  { id: userID }
    })
  }



  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        p: 3,
        maxWidth: 800,
        mx: 'auto',
        minHeight: '80vh',
      }}
    >
      {/* Avatar and Name */}
      <Avatar
        src={state?.avatar_url}
        sx={{
          width: 120,
          height: 120,
          mb: 2,
          bgcolor: state?.avatar_url ? 'transparent' : 'primary.main',
        }}
      >
        {!state?.avatar_url && <PersonIcon sx={{ fontSize: 60 }} />}
      </Avatar>
      <Typography variant="h5" gutterBottom>
        {state.firstName} {state.lastName}
      </Typography>

      {/* 3x2 Box Grid Layout */}
      <Box
        sx={{
          display: 'flex',
          flexWrap: 'wrap',
          gap: 2,
          mt: 3,
          width: '100%',
        }}
      >
        {/* Row 1 */}
        <Box sx={{ width: { xs: '100%', sm: '48%' } }}>
          <Paper elevation={3} sx={{ p: 2, minHeight: 70 }}>
            <Typography variant="subtitle2">First Name</Typography>
            <Typography variant="body2">{state.firstName}</Typography>
          </Paper>
        </Box>
        <Box sx={{ width: { xs: '100%', sm: '48%' } }}>
          <Paper elevation={3} sx={{ p: 2, minHeight: 70 }}>
            <Typography variant="subtitle2">Last Name</Typography>
            <Typography variant="body2">{state.lastName}</Typography>
          </Paper>
        </Box>

        {/* Row 2 */}
        <Box sx={{ width: { xs: '100%', sm: '48%' } }}>
          <Paper elevation={3} sx={{ p: 2, minHeight: 70 }}>
            <Typography variant="subtitle2">Email</Typography>
            <Typography variant="body2">{state.email}</Typography>
          </Paper>
        </Box>
        <Box sx={{ width: { xs: '100%', sm: '48%' } }}>
          <Paper elevation={3} sx={{ p: 2, minHeight: 70 }}>
            <Typography variant="subtitle2">Phone</Typography>
            <Typography variant="body2">{state.phoneNumber}</Typography>
          </Paper>
        </Box>

        {/* Row 3 */}
        <Box sx={{ width: { xs: '100%', sm: '48%' } }}>
          <Paper elevation={3} sx={{ p: 2, minHeight: 85 }}>
            <Typography variant="subtitle2">Gender</Typography>
            <Typography variant="body2">{state.gendar}</Typography>
          </Paper>
        </Box>
        <Box sx={{ width: { xs: '100%', sm: '48%' } }}>
          <Paper elevation={3} sx={{ p: 2, minHeight: 85 }}>
            <Typography variant="subtitle2" gutterBottom>
              Roles
            </Typography>
            <Box display="flex" flexWrap="wrap" gap={1}>
              {state?.roles.map((role, i) => (
                <Chip key={i} label={role.roleName} color="success" size="small" />
              ))}
            </Box>
          </Paper>
        </Box>
      </Box>
    </Box>
  );
};

export default Profile;
