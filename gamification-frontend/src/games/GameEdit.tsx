import { DateTimeInput, EditBase, Form, NumberInput, ReferenceInput, SelectInput, TextInput, useEditContext, CheckboxGroupInput, FormDataConsumer, BooleanInput, useStore, useNotify, useRefresh, useRedirect, Button, Confirm, useDeleteMany, useListContext, useUnselectAll, TopToolbar, ToolbarProps, DeleteButton, SaveButton, ToolbarClasses, Toolbar, useDelete, SelectArrayInput } from 'react-admin';
import { Card, CardContent, Box, Theme, useMediaQuery } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { Game } from '../types';
import { Children, useState } from 'react';
import clsx from 'clsx';

export const GameEdit = () => {
    const [gameName, setGameName] = useStore('game.name');
    const [gameId] = useStore('game.selected');
    const notify = useNotify();
    const redirect = useRedirect();
    const options = { meta: { gameId: gameId } };

    const transform = (data: Game) => {{
        if (!data.expiry) {
            data.expiration = 0;
        }
         // convert date to long.
         console.log(typeof data.expiration);
         if (data.expiration != 0) {
             if ( typeof(data.expiration)==='object') {
                 data.expiration = data.expiration.getTime();
             }                
         }
        return data
    }}

    const onSuccess = (data:any) => {
        console.log('on success');
        setGameName(data.name);
        notify(`Game updated successfully`); // default message is 'ra.notification.updated'
        // redirect('list', 'game');
        redirect('/game/' + gameId + '/show');
    };
    
    return (
    <EditBase
        mutationMode='pessimistic' 
        redirect="list"
        transform={transform}
        mutationOptions={{ ...options, onSuccess }}
        queryOptions={ options }
         
        >
        <GameEditContent />
    </EditBase>
    );
}

const GameEditContent = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    const { isLoading, record } = useEditContext<Game>();
    if (isLoading || !record) return null;
    record.expiry = record.expiration!= 0;
    record.hide = record.settings.challengeSettings != undefined && record.settings.challengeSettings.disclosure != undefined &&  record.settings.challengeSettings.disclosure.startDate != undefined;
    const dateFormatter = (data:any) => {
       console.log(typeof data);
        if (isNaN(data)) {
           data = data.toDate();
        }
        return data;
    }
   
    return (
        <Box mt={2} display="flex">
            <Box flex="1">
                <Form>
                    <Card>
                        <CardContent>
                            <Box width={630}>
                                <TextInput label="Name" source="name" fullWidth />
                            </Box>
                            <Box width={630}>
                                Expiration
                                <BooleanInput source="expiry"  /> 
                                 {/* parse={parseExpiry(record)}  */}
                            </Box>
                            <FormDataConsumer>
                                {({ formData, ...rest }) => formData.expiry &&
                                    <Box mt={2} width={630}>
                                        <DateTimeInput source="expiration" />
                                    </Box>
                                }
                            </FormDataConsumer>
                            <Box width={630}>
                                Challenge visibility
                                <BooleanInput source="hide" />
                            </Box>
                            <FormDataConsumer>
                                {({ formData, ...rest }) => formData.hide &&
                                    <Box width={630}>
                                        Disclosure date<br />
                                        <DateTimeInput source="settings.challengeSettings.disclosure.startDate" />
                                    </Box> &&
                                    <Box width={630}>
                                        Frequency<br />
                                        <NumberInput source="settings.challengeSettings.disclosure.frequency.value" />&nbsp;
                                        <SelectInput source="settings.challengeSettings.disclosure.frequency.unit" choices={[{ id: 'DAY', name: 'DAY' }, { id: 'HOUR', name: 'HOUR' }, { id: 'MINUTE', name: 'MINUTE' }]} />
                                    </Box>
                                }
                            </FormDataConsumer>
                            <Box>
                                <ReferenceInput source="notifyPCName" queryOptions={options} reference="pointconcepts" perPage={1000}>
                                    <SelectArrayInput
                                        label="Point Concept"
                                        optionText={(pointconcept: any) =>
                                            `${pointconcept.pc.name}`
                                        }
                                        optionValue='pc.name'                                        
                                    />
                                </ReferenceInput>
                            </Box>
                            <Box width={630}>
                                <TextInput source="challengeChoiceConfig.cronExpression" />
                            </Box>
                        </CardContent>
                        {/* <Toolbar /> */}
                        <GameEditToolbar />
                    </Card>
                </Form>
            </Box>
        </Box>
    );
};

const GameEditToolbar = (props: ToolbarProps) => {
    const { children, className, resource, ...rest } = props;
    const isXs = useMediaQuery<Theme>(theme => theme.breakpoints.down('sm'));
    const notify = useNotify();
    const redirect = useRedirect();
    const [gameId, setGameId] = useStore('game.selected');
    const [gameName, setGameName] = useStore('game.name');
    const [open, setOpen] = useState(false);
    const handleClick = () => setOpen(true);
    const [deleteOne, { isLoading }] = useDelete(
        'game',
        { id: gameId, meta: { gameId: gameId } }
    );
    const handleDialogClose = () => setOpen(false);
    const handleConfirm = () => {
        deleteOne();
        setOpen(false);
        //clear store.
        setGameName(null);
        setGameId(null);
        notify(`Game ` + gameName + ` deleted successfully`);
        redirect('list', 'game');
    };

    return (
        <Toolbar
            className={clsx(
                {
                    [ToolbarClasses.mobileToolbar]: isXs,
                    [ToolbarClasses.desktopToolbar]: !isXs,
                },
                className
            )}
            role="toolbar"
            {...rest}
        >
            {Children.count(children) === 0 ? (
                <div className={ToolbarClasses.defaultToolbar}>
                    <SaveButton />
                    <Button label="Remove" endIcon={<DeleteIcon />} onClick={handleClick} sx={{color:'red'}}/>
                <Confirm
                    isOpen={open}
                    loading={isLoading}
                    title="Game Deletion"
                    content="Are you sure you want to delete this game?"
                    onConfirm={handleConfirm}
                    onClose={handleDialogClose}
                />
                </div>
            ) : (
                children
            )}
        </Toolbar>
    );
};