import * as React from 'react';
import { AppBar, Button, useRedirect, useStore } from 'react-admin';
import Typography from '@mui/material/Typography';
import SettingsApplicationsIcon from '@mui/icons-material/SettingsApplications';

export const MyAppBar = function (props:any) {
    const [gameName] = useStore('game.name');
    const [gameId] = useStore('game.selected');
    const [role, getRole] = useStore('user.role');
    const redirect = useRedirect();
  
    return (
        <AppBar
            sx={{
                "& .RaAppBar-title": {
                    flex: 1,
                    textOverflow: "ellipsis",
                    whiteSpace: "nowrap",
                    overflow: "hidden",
                },
            }}
            {...props}
        >
            <Typography
                variant="h6"
                color="inherit"
                // style={{ width:600}}
                style={{ width:'35%' }}
                id="react-admin-title"
            />

            <Typography
                variant="h5"
                color="inherit"
                id="react-admin-title2"
                // style={{ width:1200}}
                style={{ width:'60%' }}
            > {gameName} </Typography>

            {gameName ? <Button style={{ minWidth: 'auto' }} startIcon={<SettingsApplicationsIcon style={{ fontSize: '24px', fill: 'white'}}/>} onClick={() => {
                console.log('/game/' + gameId + '/show');
                redirect('/game/' + gameId + '/show');
            }
            }></Button> : ''}
           
            <Typography>{role}</Typography>
        </AppBar>
    );
}

// export default MyAppBar;