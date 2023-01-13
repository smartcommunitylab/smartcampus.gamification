import {
    ShowBase, useShowContext, List as RaList,
    EditButton, useStore, ArrayField, Datagrid, TextField, RichTextField, DateField, ReferenceManyField, ReferenceOneField, DeleteButton, useRecordContext,
} from 'react-admin';
import { Box, Card, CardContent, Typography } from '@mui/material';
import { dateFormatter } from '../misc/Utils';

export const PointConceptShow = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };

    return (<ShowBase queryOptions={options}>
        <PointConceptShowContent />
    </ShowBase>
    );

}

const PointConceptShowContent = () => {
    const { record, isLoading } = useShowContext<any>();

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
                                <Typography >Score: {record.score}</Typography>
                                <br />
                                <Typography >Periods</Typography>
                                <br />
                                <ArrayField source="periods">
                                    <Datagrid bulkActionButtons={false}>
                                        <TextField source="name" />
                                        <DateField source="start" />
                                        <DateField source="end" />
                                        <CustomPeriodField title='period in days' source="period" />
                                        <TextField source="capacity" />
                                    </Datagrid>
                                </ArrayField>
                            </Box>
                        </Box>
                    </CardContent>
                </Card>
            </Box>
            <Box>
                <DeleteButton />
            </Box>
        </Box>
    );
};


const CustomPeriodField = (props: any) => {
    const record = useRecordContext();
    let dayMillis = 24 * 3600 * 1000;
    let period = record.period / dayMillis;
    return (
        <>
            {
                period
            }
        </>

    );
}