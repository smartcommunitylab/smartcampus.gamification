import * as React from 'react';
import {
    List as RaList,
    SimpleListLoading,
    ReferenceField,
    TextField,
    useListContext,
    ExportButton,
    SortButton,
    TopToolbar,
    CreateButton,
    Pagination,
    useGetIdentity,
    BulkActionsToolbar,
    BulkDeleteButton,
    RecordContextProvider,
    Datagrid,
    EditButton,
    ShowButton,
    useStore,
    DeleteButton,
    BulkExportButton,
    Button,
    Confirm,
    useDeleteMany,
    useUnselectAll,
} from 'react-admin';
import {
    List,
    ListItem,
    ListItemAvatar,
    ListItemIcon,
    ListItemSecondaryAction,
    ListItemText,
    Checkbox,
    Typography,
} from '@mui/material';
import { Link } from 'react-router-dom';
import { formatDistance } from 'date-fns';
import { matchPath, useLocation } from 'react-router';
import { Status } from '../misc/Status';
import DeleteIcon from '@mui/icons-material/Delete';
import { Task } from '../types';
import { BulkActionButtons } from '../misc/BulkActionButtons';

const TaskListContent = () => {

    const {
        data: tasks,
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
                bulkActionButtons={<BulkActionButtons title="Task" resource="tasks" />}
                sx={{
                    '& .RaDatagrid-headerCell': {
                        fontWeight: 600,
                        // backgroundColor: 'cornflowerblue',
                        // color: 'white'
                    },

                }}>
                <TextField label="Task Name" source="id" />
                <TextField label="Type" source="type" />
                <TextField label="PointConcept" source="pointConceptName" />
                <span style={{ float: 'right' }}>
                    <ShowButton />
                    <EditButton />
                </span>
            </Datagrid>
            {/* <List>
                {tasks.map(obj => (
                    <RecordContextProvider key={obj.id} value={obj.task}>
                        <ListItem style={{ paddingBottom: 0, paddingTop: 0 }}
                            button
                            component={Link}
                            to={`/tasks/${obj.id}/show`}
                            
                        >
                            <ListItemIcon>
                                <Checkbox
                                    edge="start"
                                    checked={selectedIds.includes(obj.id)}
                                    tabIndex={-1}
                                    disableRipple
                                    onClick={e => {
                                        e.stopPropagation();
                                        onToggleItem(obj.id);
                                    }}
                                />
                            </ListItemIcon>
                            <ListItemAvatar>
                              
                            </ListItemAvatar>
                            <ListItemText
                                primary={`${obj.task.name}`}
                            />
                        </ListItem>
                    </RecordContextProvider>
                ))}
            </List> */}
        </>
    );
};

const TaskListActions = () => (
    <TopToolbar>
        {/* <SortButton fields={['last_name', 'first_name', 'last_seen']} /> */}
        {/* <ExportButton /> */}
        <CreateButton
            variant="contained"
            label="New Task"
            sx={{ marginLeft: 2 }}
        />
        {/* <PostBulkActionButtons/>      */}
    </TopToolbar>
);

export const TaskList = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    return (
        <RaList
            queryOptions={options}
            actions={<TaskListActions />}
            perPage={25}
            pagination={<Pagination rowsPerPageOptions={[10, 25, 50, 100]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
        >
            <TaskListContent />
        </RaList>
    );
};

// const PostBulkActionButtons = () => {
//     const [gameId] = useStore('game.selected');
//     const options = { meta: { gameId: gameId } };
//     const unselectAll = useUnselectAll('tasks');
//     const [open, setOpen] = React.useState(false);
//     const { selectedIds } = useListContext('tasks');
//     const handleClick = () => setOpen(true);

//     const [deleteMany, { isLoading }] = useDeleteMany(
//         'tasks',
//         { ids: selectedIds, meta: { gameId: gameId } }
//     );
//     const handleDialogClose = () => setOpen(false);
//     const handleConfirm = () => {
//         deleteMany();
//         setOpen(false);
//         unselectAll();
//     };


//     return (
//         <>
//             <>
//                 <Button label="Remove task(s)" endIcon={<DeleteIcon />} onClick={handleClick} sx={{color:'red'}}/>
//                 <Confirm
//                     isOpen={open}
//                     loading={isLoading}
//                     title="Task Deletion"
//                     content="Are you sure you want to delete these task(s)?"
//                     onConfirm={handleConfirm}
//                     onClose={handleDialogClose}
//                 />
//             </>
//         </>


//     );
// }