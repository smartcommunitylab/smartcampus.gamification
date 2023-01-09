import * as React from 'react';
import { AppBar, useStore } from 'react-admin';
import Typography from '@mui/material/Typography';

export const MyAppBar = function (props:any) {
    const [gameName, getGameName] = useStore('game.name');
    const [role, getRole] = useStore('user.role');
  
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

            <Typography>{role}</Typography>
        </AppBar>
    );
}

// export default MyAppBar;