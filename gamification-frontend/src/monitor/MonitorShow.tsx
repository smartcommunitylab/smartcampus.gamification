import {
    ShowBase, useShowContext,
    EditButton, useStore, ArrayField, Datagrid, TextField, RichTextField, DateField, RecordContextProvider, useRecordContext
} from 'react-admin';
import { List, Box, Card, CardContent, ListItem, ListItemText, Typography } from '@mui/material';
import { isNoSubstitutionTemplateLiteral } from 'typescript';



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
                                    <Datagrid bulkActionButtons={false}>
                                        <TextField source="name" />
                                        <TextField source="score" />
                                        <PeriodField source="periods.daily.instances"></PeriodField>
                                        {/* <ArrayField source="periods.daily.instances" >
                                                <Datagrid  bulkActionButtons={false}>
                                                    <DateField source="start" />
                                                    <DateField source="end" />
                                                </Datagrid>
                                            </ArrayField>
                                            <ArrayField source="periods.weekly.instances" >
                                                <Datagrid  bulkActionButtons={false}>
                                                    <DateField source="start" />
                                                    <DateField source="end" />
                                                </Datagrid>
                                            </ArrayField> */}
                                    </Datagrid>
                                </ArrayField >

                            </Box>
                            <br />
                            <Box>
                                <Typography sx={{ fontWeight: 400, color: 'rgb(55,159,244)', borderBottom: 1, borderColor: 'grey.300' }}>Badges</Typography>
                                <br />
                                <ArrayField source="state.BadgeCollectionConcept">
                                    <Datagrid bulkActionButtons={false}>
                                        <TextField source="name" />
                                        <TextField source="score" />
                                        <RichTextField source="badgeEarned" />
                                    </Datagrid>
                                </ArrayField >
                            </Box>
                            <br />
                            <Box>
                                <Typography sx={{ fontWeight: 400, color: 'rgb(55,159,244)', borderBottom: 1, borderColor: 'grey.300' }}>Levels</Typography>
                                <ArrayField source="levels">
                                    <Datagrid bulkActionButtons={false}>
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
                                <TextField source="challengeActivationActions" />
                                <ArrayField source="challengeChoices">
                                    <Datagrid bulkActionButtons={false}>
                                        <TextField source="modelName" />
                                        <TextField source="state" />
                                    </Datagrid>
                                </ArrayField >
                            </Box>
                            <br />
                            <Box>
                                <Typography sx={{ fontWeight: 400, color: 'rgb(55,159,244)', borderBottom: 1, borderColor: 'grey.300' }}>Challenges</Typography>
                                <ArrayField source="state.ChallengeConcept">
                                    <Datagrid bulkActionButtons={false}>
                                        <DateField source="dateCompleted" />
                                        <TextField source="group" />
                                        <TextField source="modelName" />
                                        <TextField source="name" />
                                        <DateField source="start" />
                                    </Datagrid>
                                </ArrayField >
                            </Box>
                            <br />
                            <Box>
                                <Typography sx={{ fontWeight: 400, color: 'rgb(55,159,244)', borderBottom: 1, borderColor: 'grey.300' }}>Custom Data</Typography>

                                {/* <ul>
    {record.customData.map((value:any, index:any) => {
      return <li key={index}>{value}</li>
    })}
  </ul> */}
                                {/* <List>
                                    {record.customData.map((customData:any) => (
                                        <RecordContextProvider>
                                            <ListItem>
                                                <ListItemText primary={customData.key} secondary={customData.value}></ListItemText>
                                               </ListItem>
                                        </RecordContextProvider>
                                    ))}
                                </List> */}
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
    console.log(props);
    return (
         <ul>
            {
                Object.entries(record.periods.daily.instances).map((elem:any) => (
                    <li>
                        start: {dateFormatter(elem[1].start)}&nbsp;end: {dateFormatter(elem[1].end)}&nbsp;score: {elem[1].score}
                    </li>
                    
                ))
            }
        </ul>

    );
}

const dateFormatRegex = /^\d{4}-\d{2}-\d{2}$/;
const dateParseRegex = /(\d{4})-(\d{2})-(\d{2})/;

const dateFormatter = (value:any) => {
    // null, undefined and empty string values should not go through dateFormatter
    // otherwise, it returns undefined and will make the input an uncontrolled one.
    if (value == null || value === '') return '';
    if (value instanceof Date) return convertDateToString(value);
    // Valid dates should not be converted
    if (dateFormatRegex.test(value)) return value;

    return convertDateToString(new Date(value));
};

const convertDateToString = (value:any) => {
    // value is a `Date` object
    if (!(value instanceof Date) || isNaN(value.getDate())) return '';
    const pad = '00';
    const yyyy = value.getFullYear().toString();
    const MM = (value.getMonth() + 1).toString();
    const dd = value.getDate().toString();
    return `${yyyy}-${(pad + MM).slice(-2)}-${(pad + dd).slice(-2)}`;
};
