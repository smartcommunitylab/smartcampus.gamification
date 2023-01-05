import * as React from 'react';
import {
    List as RaList,
    SimpleListLoading, TextField,
    useListContext, TopToolbar,
    CreateButton,
    Pagination, Datagrid,
    EditButton,
    ShowButton,
    useStore, Button,
    Confirm,
    useDeleteMany,
    useUnselectAll
} from 'react-admin';
import DeleteIcon from '@mui/icons-material/Delete';
import { Box } from '@mui/material';
import { BulkActionButtons } from '../misc/BulkActionButtons';

const LevelListContent = () => {

    const {
        data: Level,
        isLoading,
        onToggleItem,
        selectedIds,
    } = useListContext<any>();
    if (isLoading) {
        return <SimpleListLoading hasLeftAvatarOrIcon hasSecondaryText />;
    }
    const now = Date.now();

    return (
        <>
            <Datagrid
                bulkActionButtons={<BulkActionButtons title="Level" resource="levels"/>}
                sx={{
                    '& .RaDatagrid-headerCell': {
                        fontWeight: 600,
                    },

                }}>
                <TextField  label="Level Name" source="id" />
                <span style={{ float: 'right' }}>
                    <ShowButton />&nbsp;&nbsp;&nbsp;
                    <EditButton />
                </span>
            </Datagrid>
        </>
    );
};

const LevelListActions = () => (
    <TopToolbar>
        <CreateButton
            variant="contained"
            label="New Level"
            sx={{ marginLeft: 2 }}
        />
    </TopToolbar>
);

export const LevelList = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    return (
        <RaList
            queryOptions={options}
            actions={<LevelListActions />}
            perPage={25}
            pagination={<Pagination rowsPerPageOptions={[10, 25, 50, 100]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
        >
            <LevelListContent />
        </RaList>
    );
};

const PostBulkActionButtons = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    const unselectAll = useUnselectAll('levels');
    const [open, setOpen] = React.useState(false);
    const { selectedIds } = useListContext('levels');
    const handleClick = () => setOpen(true);

    const [deleteMany, { isLoading }] = useDeleteMany(
        'levels',
        { ids: selectedIds, meta: { gameId: gameId } }
    );
    const handleDialogClose = () => setOpen(false);
    const handleConfirm = () => {
        deleteMany();
        setOpen(false);
        unselectAll();
    };


    return (
        <>
            <>
                <Button label="Remove level(s)" endIcon={<DeleteIcon />} onClick={handleClick} sx={{ color: 'red' }} />
                <Confirm
                    isOpen={open}
                    loading={isLoading}
                    title="Level Deletion"
                    content="Are you sure you want to delete these level(s)?"
                    onConfirm={handleConfirm}
                    onClose={handleDialogClose}
                />
            </>
        </>


    );
}


const Spacer = () => <Box width={20} component="span" />;