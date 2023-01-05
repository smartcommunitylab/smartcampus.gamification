import { Box } from '@mui/material';
import { InputProps, useInput } from "react-admin";
import AceEditor from "react-ace";
import "ace-builds/src-noconflict/mode-drools";
import "ace-builds/src-noconflict/theme-github";


export const DroolRuleEditor = (props:InputProps) => {

    const { onChange, onBlur, ...rest } = props;
    const {
        field,
        fieldState: { isTouched, invalid, error },
        formState: { isSubmitted },
        isRequired
    } = useInput({
        // Pass the event handlers to the hook but not the component as the field property already has them.
        // useInput will call the provided onChange and onBlur in addition to the default needed by react-hook-form.
        onChange,
        onBlur,
        ...props,
    });

    return (
        <span>
            <Box>
                <AceEditor name={field.name} onChange={field.onChange} mode="drools" value={field.value} width="100%" wrapEnabled ></AceEditor>
            </Box>
        </span>
    );
    
};