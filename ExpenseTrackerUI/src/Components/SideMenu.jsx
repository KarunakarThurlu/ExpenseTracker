import React, { useContext } from 'react';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import GridViewIcon from '@mui/icons-material/GridView';
import WorkspacesOutlinedIcon from '@mui/icons-material/WorkspacesOutlined';
import PeopleAltOutlinedIcon from '@mui/icons-material/PeopleAltOutlined';
import CurrencyRupeeIcon from '@mui/icons-material/CurrencyRupee';
import { Link } from 'react-router-dom';

import { Avatar } from '@mui/material';


const SideMenu = ({onClose}) =>{


const userRoles = localStorage.getItem('roles'); // or get from context if available

return(
  <div onClick={onClose} style={{color: 'black'}}>
    <List >
      <ListItem component={Link} to='/profile'>
        <ListItemIcon>
          <Avatar
            alt="Profile Image"
            //src={state.avatar_url}
            sx={{ width: 30, height: 30, cursor: 'pointer' }} 
          />
        </ListItemIcon>
        <ListItemText primary="Profile" sx={{color: '#fbc600'}}/>
      </ListItem>

      <ListItem component={Link} to='/Dashboard'>
        <ListItemIcon>
          <GridViewIcon />
        </ListItemIcon>
        <ListItemText primary="Dashboard" sx={{color: '#fbc600'}}/>
      </ListItem>

      <ListItem component={Link} to='/expenses'>
        <ListItemIcon>
          <CurrencyRupeeIcon />
        </ListItemIcon>
        <ListItemText primary="Expenses" sx={{color: '#fbc600'}}/>
      </ListItem>

      {userRoles.split(",").some(role => role === 'ROLE_SUPER_ADMIN') && (
        <ListItem component={Link} to='/tenants'>
          <ListItemIcon>
            <WorkspacesOutlinedIcon />
          </ListItemIcon>
          <ListItemText primary="Tenants" sx={{color: '#fbc600'}}/>
        </ListItem>
      )}

       {(userRoles.split(",").some(role => role === 'ROLE_SUPER_ADMIN') || userRoles.split(",").some(role => role === 'ROLE_ADMIN')) && (<ListItem component={Link} to='/users'>
        <ListItemIcon>
          <PeopleAltOutlinedIcon />
        </ListItemIcon>
        <ListItemText primary="Users" sx={{color: '#fbc600'}}/>
      </ListItem>)}
    </List>
  </div>
)
}
export default SideMenu;