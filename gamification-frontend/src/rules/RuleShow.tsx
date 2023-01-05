import {
    ShowBase, useShowContext,
    EditButton, useStore, SimpleShowLayout
} from 'react-admin';
import { Box, Typography } from '@mui/material';

import { Rule } from '../types';
import AceEditor from "react-ace";
import "ace-builds/src-noconflict/mode-drools";
import "ace-builds/src-noconflict/theme-github";

export const RuleShow = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    
    return (<ShowBase queryOptions={ options }>
        <RuleShowContent />
    </ShowBase>
    );

}

const RuleShowContent = () => {
    const { record, isLoading } = useShowContext<Rule>();
   
    if (isLoading || !record?.content) return null;
    return (
       <SimpleShowLayout>
            <Box mt={2} display="flex">
                <Box display="flex" style={{ width:'95%' }}>
                    <Typography>{record.name}</Typography>
                </Box>
                <Box>
                    <EditButton to={`/rules/${record.id}`} />
                </Box>
            </Box>
            <br />
            <AceEditor mode="drools" readOnly theme="github" value={record.content} wrapEnabled width="100%"></AceEditor>
        </SimpleShowLayout>
    );
};
