import * as React from 'react';
import {
    useListContext, useStore, Button,
    Confirm,
    useDeleteMany,
    useUnselectAll
} from 'react-admin';
import DeleteIcon from '@mui/icons-material/Delete';

export const BulkActionButtons = (props:any) => { //title, resource,
    const [gameId] = useStore('game.selected');
    const unselectAll = useUnselectAll(props.resource);
    const [open, setOpen] = React.useState(false);
    const { selectedIds } = useListContext(props.resource);
    const handleClick = () => setOpen(true);

    const [deleteMany, { isLoading }] = useDeleteMany(
        props.resource,
        { ids: selectedIds, meta: { gameId: gameId } }
    );
    const handleDialogClose = () => setOpen(false);
    const handleConfirm = () => {
        deleteMany();
        setOpen(false);
        unselectAll();
    };

    let title = props.title + " Deletion";
    let content = "Are you sure you want to delete this " + props.title + "(s) ?"
    let label = "Remove " + props.title + "(s)"
    return (
        <>
            <>
                <Button label={label} endIcon={<DeleteIcon />} onClick={handleClick} sx={{ color: 'red' }} />
                <Confirm
                    isOpen={open}
                    loading={isLoading}
                    title= {title}
                    content= {content}
                    onConfirm={handleConfirm}
                    onClose={handleDialogClose}
                />
            </>
        </>


    );
}