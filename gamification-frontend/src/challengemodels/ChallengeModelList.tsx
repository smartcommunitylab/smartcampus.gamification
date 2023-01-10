import { List, ListItem, ListItemIcon, Checkbox, ListItemAvatar, Avatar, ListItemText, ListItemSecondaryAction, Typography, ListSubheader } from '@mui/material';
import { formatDistance } from 'date-fns';
import {
    BulkActionsToolbar,
    CreateButton, Datagrid, EditButton, Link, List as RaList, ListActions, Pagination, RecordContextProvider, ReferenceField, ShowButton, SimpleListLoading, TextField, TopToolbar, useListContext, useRecordContext, useStore
} from 'react-admin';
import { BulkActionButtons } from '../misc/BulkActionButtons';
import { Status } from '../misc/Status';

const ChallengeModelListContent = () => {

    const {
        data: chmodels,
        isLoading,
        onToggleItem,
        selectedIds,
    } = useListContext<any>();
    if (isLoading) {
        return <SimpleListLoading hasLeftAvatarOrIcon hasSecondaryText />;
    }
    let groupChallenges: string[] = ['groupCompetitivePerformance', 'groupCompetitiveTime', 'groupCooperative'];

    // return (
    //     <>
    //         <Datagrid
    //             bulkActionButtons={<BulkActionButtons title="Challenge Model" resource="challengemodels" />}
    //             isRowSelectable={record => (!groupChallenges.includes(record.name))} 
    //             sx={{
    //                 '& .RaDatagrid-headerCell': {
    //                     fontWeight: 600,
    //                 },

    //             }}>
    //             <TextField label="Model Name" source="name" />
    //             <span style={{ float: 'right' }}>
    //                 <ButtonPanel />
    //             </span>
    //         </Datagrid>
    //     </>
    // );

    return (
        <>
            <BulkActionsToolbar>
                <BulkActionButtons title="Challenge Model" resource="challengemodels" />
            </BulkActionsToolbar>
            <ListSubheader>Model Name</ListSubheader>
            <List>
                {chmodels.map(model => (
                    <RecordContextProvider>
                        {!groupChallenges.includes(model.name) && <ListItem dense={true} sx={{ pt: 0, pb: 0, borderBottom: 1, borderColor: 'grey.300' }} >
                            <Checkbox
                                edge="start"
                                checked={selectedIds.includes(model.name)}
                                tabIndex={-1}
                                disableRipple
                                onClick={e => {
                                    e.stopPropagation();
                                    onToggleItem(model.name);
                                }}
                            />

                            <ListItemText
                                primary={`${model.name}`}
                            />


                        </ListItem>
                        }

                    </RecordContextProvider>
                ))}
            </List>
        </>
    );
}

const ButtonPanel = () => {
    const record = useRecordContext();
    if (record.name === "groupCompetitivePerformance" ||
        record.name === "groupCompetitiveTime" ||
        record.name === "groupCooperative") {
        return (<span><ShowButton disabled />&nbsp;&nbsp;&nbsp;
            <EditButton disabled /></span>);
    }
    return <span><ShowButton to={`/challengemodels/${record.name}/show`} />&nbsp;&nbsp;&nbsp;
        <EditButton /></span>;

}

const CustomDataGridBulkAction = () => {
    const record = useRecordContext();
    if (!record) return null;
    if (record.name === "groupCompetitivePerformance" ||
        record.name === "groupCompetitiveTime" ||
        record.name === "groupCooperative") {
        return (
            false
        );
    }
    return (
        <BulkActionButtons title="Challenge Model" resource="challengemodels" />
    );
}

const ChallengeModelListActions = () => (
    <TopToolbar>
        <CreateButton
            variant="contained"
            label="New Challenge model"
            sx={{ marginLeft: 2 }}
        />
    </TopToolbar>
);

export const ChallengeModelList = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    return (
        <RaList
            queryOptions={options}
            actions={<ChallengeModelListActions />}
            perPage={25}
            pagination={<Pagination rowsPerPageOptions={[10, 25, 50, 100]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
        >
            <ChallengeModelListContent />
        </RaList>
    );
};
