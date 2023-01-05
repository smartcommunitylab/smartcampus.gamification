import {
    ShowBase, useShowContext,
    EditButton, useStore
} from 'react-admin';
import { Box, Card, CardContent, Typography } from '@mui/material';

import { Task } from '../types';

export const TaskShow = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };

    return (<ShowBase queryOptions={options}>
        <TaskShowContent />
    </ShowBase>
    );
}

const TaskShowContent = () => {
    const { record, isLoading } = useShowContext<Task>();

    if (isLoading || !record) return null;
    return (
        <Box mt={2} display="flex">
            <Box flex="1">
                <Card>
                    <CardContent>
                        <Box display="flex" width={630}>
                            <Box>
                                <Typography >Name: {record.task.name}</Typography>
                                <br />
                                <Typography > Classification Name: {record.task.classificationName}</Typography>
                                <br />
                                <Typography > Number elements in classification : {record.task.itemsToNotificate}</Typography>
                                <br />
                                <Typography >PointConcept : {record.pointConceptName}</Typography>
                                <br />
                                <Typography >Type : {record.type}</Typography>
                            </Box>
                        </Box>
                    </CardContent>
                </Card>
            </Box>
            <Box>
                <EditButton label="Edit Task" to={`/tasks/${record.task.name}`} />
            </Box>
        </Box>
    );
};
