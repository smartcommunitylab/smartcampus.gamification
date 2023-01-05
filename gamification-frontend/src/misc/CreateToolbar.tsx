import * as React from 'react';
import { SaveButton, Toolbar, ToolbarClasses, ToolbarProps } from 'react-admin';
import { Theme, useMediaQuery } from '@mui/material';
import clsx from 'clsx';


export const CreateToolbar = (props: ToolbarProps) => {
    const { children, className, resource, ...rest } = props;
    const isXs = useMediaQuery<Theme>(theme => theme.breakpoints.down('sm'));
    return (
        <Toolbar
            className={clsx(
                {
                    [ToolbarClasses.mobileToolbar]: isXs,
                    [ToolbarClasses.desktopToolbar]: !isXs,
                },
                className
            )}
            role="toolbar"
            {...rest}
        >
            {React.Children.count(children) === 0 ? (
                <div className={ToolbarClasses.defaultToolbar}>
                    <SaveButton />
                </div>
            ) : (
                children
            )}
        </Toolbar>
    );
};
