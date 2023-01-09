import {
    List as RaList,
    SimpleListLoading, TextField,
    useListContext, TopToolbar,
    CreateButton,
    Pagination, Datagrid,
    EditButton,
    ShowButton,
    useStore,
    useRecordContext
} from 'react-admin';
import { BulkActionButtons } from '../misc/BulkActionButtons';

const ChallengeModelListContent = () => {

    const {
        data,
        isLoading,
        onToggleItem,
        selectedIds,
    } = useListContext<any>();
    if (isLoading) {
        return <SimpleListLoading hasLeftAvatarOrIcon hasSecondaryText />;
    }
    const now = Date.now();

    let groupChallenges:string[] = ['groupCompetitivePerformance','groupCompetitiveTime','groupCooperative'];

    return (
        <>
            <Datagrid
                bulkActionButtons={<BulkActionButtons title="Challenge Model" resource="challengemodels" />}
                isRowSelectable={record => (!groupChallenges.includes(record.name))} 
                sx={{
                    '& .RaDatagrid-headerCell': {
                        fontWeight: 600,
                    },

                }}>
                <TextField label="Model Name" source="name" />
                <span style={{ float: 'right' }}>
                    <ButtonPanel />
                </span>
            </Datagrid>
        </>
    );
};

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
