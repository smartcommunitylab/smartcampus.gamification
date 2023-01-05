import { Box } from '@mui/material';
import {
    TextInput, useStore
} from 'react-admin';
import AceEditor from "react-ace";
import "ace-builds/src-noconflict/mode-drools";
import "ace-builds/src-noconflict/theme-github";

export const RuleInputs = () => {
    const [gameId] = useStore('game.selected');
    return (
        <Box flex="1" mt={-1}>
            <Box display="flex" width={630}>
                <TextInput label="Name" source="name" fullWidth />
            </Box>
            <Box mt={2} width={630}>
                <AceEditor mode="drools" theme="github" value="{`content`}"></AceEditor>
            </Box>

        </Box>
    );
};