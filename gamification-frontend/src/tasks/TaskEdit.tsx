import * as React from 'react';
import { Button, Confirm, EditBase, Form, SaveButton, Toolbar, ToolbarClasses, ToolbarProps, useDelete, useEditContext, useNotify, useRedirect, useStore } from 'react-admin';
import { Card, CardContent, Box, Theme, useMediaQuery } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { TaskInputs } from './TaskInputs';
import { Task } from '../types';
import clsx from 'clsx';
import { EditToolbar } from '../misc/EditToolbar';

export const TaskEdit = () => {
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();
    const options = { meta: { gameId: gameId } };

    const transform = (data: any) => {{
        console.log(data);
        let body = createTaskBody(data, gameId);
        console.log(body);
        return body;
    }}

    const onSuccess = (data:any) => {
        notify(`Task updated successfully`);
        redirect('/tasks/' + data.id + '/show');
    };

    return (<EditBase  mutationMode='pessimistic' 
     transform={transform}
      mutationOptions={{ ...options, onSuccess }}
       queryOptions={ options }     
    >
        <TaskEditContent />
    </EditBase>);
}

const TaskEditContent = () => {
    const { isLoading, record } = useEditContext<Task>();
    if (isLoading || !record) return null;
    return (
        <Box mt={2} display="flex">
            <Box flex="1">
                <Form>
                    <Card>
                        <CardContent>
                            <Box>
                                <Box display="flex">
                                    <Box mr={2}>
                                    </Box>
                                    <TaskInputs />
                                </Box>
                            </Box>
                        </CardContent>
                        <EditToolbar title="Task" resourcename="tasks" targetname="list"/>
                    </Card>
                </Form>
            </Box>
 
        </Box>
    );
};
function createTaskBody(data: any, gameId:any) {
        let body: any = {};
        if (data.type === 'general') {
            body['type'] = data.type;
            body['cronExpression'] = data.task.cronExpression;
            body['name'] = data.task.name;
            body['gameId'] = gameId;
            body['itemsToNotificate'] = data.task.itemsToNotificate;
            body['itemType'] = data.pointConceptName;
            body['classificationName'] = data.task.classificationName;
        } else if (data.type === 'incremental') {
            body['name'] = data.task.name;
            body['itemType'] = data.pointConceptName;
            body['classificationName'] = data.task.classificationName;
            body['periodName'] = data.task.periodName;
            body['itemsToNotificate'] = data.task.itemsToNotificate;
            body['type'] = data.type;
            body['delayValue'] = data.task.delayValue;
            body['delayUnit'] = data.task.delayUnit;
            body['gameId'] = gameId;
        }    
        return body; 
}

// const GameEditToolbar = (props: ToolbarProps) => {
//     const { isLoading, record } = useEditContext<Task>();
//     if (isLoading || !record) return null;
//     const { children, className, resource, ...rest } = props;
//     const isXs = useMediaQuery<Theme>(theme => theme.breakpoints.down('sm'));
//     const notify = useNotify();
//     const redirect = useRedirect();
//     const [gameId, setGameId] = useStore('game.selected');
//     const [gameName, setGameName] = useStore('game.name');
//     const [open, setOpen] = React.useState(false);
//     const handleClick = () => setOpen(true);
//     const [deleteOne] = useDelete(
//         'tasks',
//         { id: record.id, meta: { gameId: gameId } }
//     );
//     const handleDialogClose = () => setOpen(false);
//     const handleConfirm = (data:any) => {
//         deleteOne();
//         setOpen(false);
//         notify(`Task deleted successfully`);
//         redirect('list', 'tasks');
//     };

//     return (
//         <Toolbar
//             className={clsx(
//                 {
//                     [ToolbarClasses.mobileToolbar]: isXs,
//                     [ToolbarClasses.desktopToolbar]: !isXs,
//                 },
//                 className
//             )}
//             role="toolbar"
//             {...rest}
//         >
//             {React.Children.count(children) === 0 ? (
//                 <div className={ToolbarClasses.defaultToolbar}>
//                     <SaveButton />
//                     <Button label="Remove" endIcon={<DeleteIcon />} onClick={handleClick} sx={{color:'red'}}/>
//                 <Confirm
//                     isOpen={open}
//                     loading={isLoading}
//                     title="Task Deletion"
//                     content="Are you sure you want to delete this task?"
//                     onConfirm={handleConfirm}
//                     onClose={handleDialogClose}
//                 />
//                 </div>
//             ) : (
//                 children
//             )}
//         </Toolbar>
//     );
// };

