import * as React from 'react';
import { Button, Confirm, SaveButton, Toolbar, ToolbarClasses, useDelete, useEditContext, useNotify, useRedirect, useStore } from 'react-admin';
import { Theme, useMediaQuery } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import clsx from 'clsx';

export const EditToolbar = (props: any) => {
    const { isLoading, record } = useEditContext<any>();
    if (isLoading || !record) return null;
    const { children, className, resource, ...rest } = props;
    const isXs = useMediaQuery<Theme>(theme => theme.breakpoints.down('sm'));
    const notify = useNotify();
    const redirect = useRedirect();
    const [gameId, setGameId] = useStore('game.selected');
    const [open, setOpen] = React.useState(false);
    const handleClick = () => setOpen(true);
    const [deleteOne] = useDelete(
        props.resourcename,
        { id: record.id, meta: { gameId: gameId } }
    );
    const handleDialogClose = () => setOpen(false);
    const handleConfirm = (data: any) => {
        deleteOne();
        setOpen(false);
        notify(props.title + ` deleted successfully`);
        redirect(props.targetname, props.resourcename);
    };

    let title = props.title + " Deletion";
    let content = "Are you sure you want to delete this " + props.title + " ?"
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
                    <SaveButton/>
                    <Button label="Remove" endIcon={<DeleteIcon />} onClick={handleClick} sx={{ color: 'red' }} />
                    <Confirm
                        isOpen={open}
                        loading={isLoading}
                        title= {title}
                        content= {content}
                        onConfirm={handleConfirm}
                        onClose={handleDialogClose}
                    />
                </div>
            ) : (
                children
            )}
        </Toolbar>
    );
};