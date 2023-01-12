import {
    ShowBase, useShowContext,
    EditButton, useStore, ArrayField, Datagrid, TextField, RichTextField, DateField, RecordContextProvider, useRecordContext
} from 'react-admin';
import { List, Box, Card, CardContent, ListItem, ListItemText, Typography } from '@mui/material';
import { isNoSubstitutionTemplateLiteral } from 'typescript';
import { dateFormatter } from '../misc/Utils';



export const MonitorShow = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };

    return (<ShowBase queryOptions={options}>
        <MonitorShowContent />
    </ShowBase>
    );

}

const MonitorShowContent = () => {
    const { record, isLoading } = useShowContext<any>();

    if (isLoading || !record) return null;
    return (
        <Box mt={2} display="flex">
            <Box flex="1">
                <Card>
                    <CardContent>
                        <Box >
                            <Box>
                                <Typography sx={{ fontWeight: 600 }} >{record.playerId}</Typography>
                            </Box>
                            <br />
                            <Box>
                                <Typography sx={{ fontWeight: 400, color: 'rgb(55,159,244)', borderBottom: 1, borderColor: 'grey.300' }}>Points</Typography>
                                <br />
                                <ArrayField source="state.PointConcept">
                                    <Datagrid sx={{ '& .RaDatagrid-headerCell': {fontWeight: 600} }} bulkActionButtons={false}>
                                     <TextField source="name" />
                                        <TextField source="score" />
                                        <PeriodField source="periods.daily.instances" period="daily"></PeriodField>
                                        <PeriodField source="periods.weekly.instances" period="weekly"></PeriodField>
                                    </Datagrid>
                                </ArrayField >

                            </Box>
                            <br />
                            <Box>
                                <Typography sx={{ fontWeight: 400, color: 'rgb(55,159,244)', borderBottom: 1, borderColor: 'grey.300' }}>Badges</Typography>
                                <br />
                                <ArrayField source="state.BadgeCollectionConcept">
                                    <Datagrid sx={{ '& .RaDatagrid-headerCell': {fontWeight: 600} }} bulkActionButtons={false}>
                                        <TextField source="name" />
                                        <CustomTextField source="score" arrayName="badgeEarned"/>
                                        <RichTextField source="badgeEarned" />
                                    </Datagrid>
                                </ArrayField >
                            </Box>
                            <br />
                            <Box>
                                <Typography sx={{ fontWeight: 400, color: 'rgb(55,159,244)', borderBottom: 1, borderColor: 'grey.300' }}>Levels</Typography>
                                <ArrayField source="levels">
                                    <Datagrid sx={{ '& .RaDatagrid-headerCell': {fontWeight: 600} }} bulkActionButtons={false}>
                                        <TextField source="levelName" />
                                        <TextField source="levelValue" />
                                        <TextField source="startLevelScore" />
                                        <TextField source="toNextLevel" />
                                        <TextField source="levelIndex" />
                                        <TextField source="endLevelScore" />
                                    </Datagrid>
                                </ArrayField >
                            </Box>
                            <br />
                            <Box>
                                <Typography sx={{ fontWeight: 400, color: 'rgb(55,159,244)', borderBottom: 1, borderColor: 'grey.300' }}>Inventory</Typography>
                                <ArrayField source="inventory.challengeChoices">
                                    <Datagrid sx={{ '& .RaDatagrid-headerCell': {fontWeight: 600} }} bulkActionButtons={false}>
                                        <TextField source="modelName" />
                                        <TextField source="state" />
                                    </Datagrid>
                                </ArrayField >
                            </Box>
                            <br />
                            <Box>
                                <Typography sx={{ fontWeight: 400, color: 'rgb(55,159,244)', borderBottom: 1, borderColor: 'grey.300' }}>Challenges</Typography>
                                <ArrayField source="state.ChallengeConcept">
                                    <Datagrid sx={{ '& .RaDatagrid-headerCell': {fontWeight: 600} }} bulkActionButtons={false}>
                                        <TextField source="name" />
                                        <TextField source="modelName" />
                                        <TextField source="fields.bonusPointType" />
                                        <TextField source="fields.bonusScore" />                                        
                                        <TextField source="fields.counterName" />
                                        <TextField source="group" />
                                        <DateField source="start" />
                                        <DateField source="dateCompleted" />                                       
                                    </Datagrid>
                                </ArrayField >
                            </Box>
                            <br />
                            <Box>
                                <Typography sx={{ fontWeight: 400, color: 'rgb(55,159,244)', borderBottom: 1, borderColor: 'grey.300' }}>Custom Data</Typography>
                                <CustomField />

                            </Box>
                        </Box>

                    </CardContent>
                </Card>
            </Box>
        </Box>
    );
};


const PeriodField = (props: any) => {
    const record = useRecordContext();
    console.log(props.period);
    let period = props.period;
    return (
         <ul>
            {
                Object.entries(record.periods[period].instances).map((elem:any) => (
                    <li>
                        start: {dateFormatter(elem[1].start)}&nbsp;end: {dateFormatter(elem[1].end)}&nbsp;score: {elem[1].score}
                    </li>
                    
                ))
            }
        </ul>

    );
}

const CustomTextField = (props: any) => {
    const record = useRecordContext();
   
    return (
         <>
            {
                record[props.arrayName].length
                // record.badgeEarned.length
            }
        </>

    );
}

const CustomField = (props: any) => {
    const record = useRecordContext();
    
    return (
         <ul>
            {
                Object.entries(record.customData).map((elem:any) => (
                    <li>
                        {elem[0]}: {String(elem[1])}
                    </li>
                    
                ))
            }
        </ul>

    );
}