import {
    ShowBase, useShowContext,
    EditButton, useStore, ArrayField, Datagrid, TextField, RichTextField
} from 'react-admin';
import { Box, Card, CardContent, Typography } from '@mui/material';

import { Level } from '../types';

export const LevelShow = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };

    return (<ShowBase queryOptions={options}>
        <LevelShowContent />
    </ShowBase>
    );

}

const LevelShowContent = () => {
    const { record, isLoading } = useShowContext<Level>();

    if (isLoading || !record) return null;
    return (
        <Box mt={2} display="flex">
            <Box flex="1">
                <Card>
                    <CardContent>
                        <Box display="flex">
                            <Box>
                                <Typography >Name: {record.name}</Typography>
                                <br />
                                <Typography >PointConcept : {record.pointConcept}</Typography>
                                <br />
                                <Typography >Thresholds</Typography>
                                <Box>
                                    <ArrayField source="thresholds">
                                        <Datagrid  bulkActionButtons={false}>
                                            <TextField source="name" />
                                            <TextField source="value" />
                                            <TextField source="config.choices" />
                                            <RichTextField  source="config.activeModels" />
                                            {/* <TextField source="config.activeModels" /> */}
                                        </Datagrid>
                                    </ArrayField >
                                </Box>
                            </Box>
                        </Box>
                    </CardContent>
                </Card>
            </Box>
            <Box>
                <EditButton label="Edit Level" to={`/levels/${record.name}`} />
            </Box>
        </Box>
    );
};
