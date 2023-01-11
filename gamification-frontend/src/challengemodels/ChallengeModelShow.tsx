import {
    ShowBase, useShowContext,
    EditButton, useStore, RichTextField, ArrayField, TextField
} from 'react-admin';
import { Box, Card, CardContent, Typography } from '@mui/material';

import { ChallengeModel } from '../types';

export const ChallengeModelShow = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };

    return (<ShowBase queryOptions={options}>
        <ChallengeModelShowContent />
    </ShowBase>
    );

}

const ChallengeModelShowContent = () => {
    const { record, isLoading } = useShowContext<ChallengeModel>();

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
                                <Typography >Variables:
                                    <ul>
                                        {record.variables.map(item => (
                                            <li>{item}</li>
                                        ))}
                                    </ul>

                                </Typography>
                            </Box>
                        </Box>
                    </CardContent>
                </Card>
            </Box>
            <Box>
                <EditButton label="Edit ChallengeModel" to={`/challengemodels/${record.id}`} />
            </Box>
        </Box>
    );
};
