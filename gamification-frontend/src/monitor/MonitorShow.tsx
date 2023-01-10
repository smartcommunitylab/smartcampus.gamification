import {
    ShowBase, useShowContext,
    EditButton, useStore, ArrayField, Datagrid, TextField, RichTextField, DateField
} from 'react-admin';
import { Box, Card, CardContent, Typography } from '@mui/material';



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
                                <Typography sx={{ fontWeight: 600}} >{record.playerId}</Typography>
                            </Box>
                            <br />                               
                            <Box>
                                <Typography sx={{ fontWeight: 400, borderBottom: 1, borderColor: 'grey.300'}}>Points</Typography>
                                <br />
                                <ArrayField source="state.PointConcept">
                                        <Datagrid  bulkActionButtons={false}>
                                            <TextField source="name" />
                                            <TextField source="score" />
                                            {/* <ArrayField source="periods" >
                                                <Datagrid  bulkActionButtons={false}>
                                                    <RichTextField source="daily" />
                                                    <RichTextField source="weekly" />
                                                </Datagrid>
                                            </ArrayField> */}
                                        </Datagrid>
                                </ArrayField >
                                                          
                            </Box>
                            <br/>
                            <Box>
                            <Typography sx={{ fontWeight: 400, borderBottom: 1, borderColor: 'grey.300'}}>Badges</Typography>
                                <br />
                                <ArrayField source="state.BadgeCollectionConcept">
                                        <Datagrid  bulkActionButtons={false}>
                                            <TextField source="name" />
                                            <TextField source="score" />
                                            <RichTextField  source="badgeEarned" />
                                        </Datagrid>
                                </ArrayField >   
                            </Box>
                            <br/>
                            <Box>
                                <Typography sx={{ fontWeight: 400, borderBottom: 1, borderColor: 'grey.300'}}>Levels</Typography>
                                <ArrayField source="levels">
                                        <Datagrid  bulkActionButtons={false}>
                                            <TextField source="levelName" />
                                            <TextField source="levelValue" />
                                            <TextField source="startLevelScore" />
                                            <TextField source="toNextLevel" />
                                            <TextField source="levelIndex" />
                                            <TextField source="endLevelScore" />
                                        </Datagrid>
                                </ArrayField >   
                            </Box>
                            <br/>
                            <Box>
                                <Typography sx={{ fontWeight: 400, borderBottom: 1, borderColor: 'grey.300'}}>Inventory</Typography>
                                <TextField source="challengeActivationActions" />
                                <ArrayField source="challengeChoices">
                                        <Datagrid  bulkActionButtons={false}>
                                            <TextField source="modelName" />
                                            <TextField source="state" />
                                        </Datagrid>
                                </ArrayField >
                            </Box>
                            <br/>
                            <Box>
                                <Typography sx={{ fontWeight: 400, borderBottom: 1, borderColor: 'grey.300'}}>Challenges</Typography>
                                <ArrayField source="state.ChallengeConcept">
                                        <Datagrid  bulkActionButtons={false}>
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
                                <Typography sx={{ fontWeight: 400, borderBottom: 1, borderColor: 'grey.300'}}>Custom Data</Typography>

                            </Box>
                            
                        </Box>
                    </CardContent>
                </Card>
            </Box>           
        </Box>
    );
};
