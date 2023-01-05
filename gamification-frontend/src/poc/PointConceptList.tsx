import {
    List as RaList,
    SimpleListLoading,
    useListContext,
    TopToolbar,
    CreateButton,
    Pagination,
    BulkActionsToolbar,
    BulkDeleteButton,
    Datagrid,
    TextField,
    useStore
} from 'react-admin';
import { PointConcept } from '../types';

const PointConceptListContent = () => {
    const {
        data: pointconcepts,
        isLoading,
        onToggleItem,
        selectedIds,
    } = useListContext<PointConcept>();
    if (isLoading) {
        return <SimpleListLoading hasLeftAvatarOrIcon hasSecondaryText />;
    }
    const now = Date.now();
    return (
        <>
            <BulkActionsToolbar>
                <BulkDeleteButton />
            </BulkActionsToolbar>
            <Datagrid
                sx={{
                    '& .RaDatagrid-headerCell': {
                        fontWeight: 600,
                        // backgroundColor: 'cornflowerblue',
                        // color: 'white'
                    }
                }}
            >
                <TextField label="Point Concept Name" source="pc.name" />
            </Datagrid>
        </>
    );
};

const PointConceptListActions = () => (
    <TopToolbar>
        <CreateButton
            variant="contained"
            label="New PointConcept"
            sx={{ marginLeft: 2 }}
        />
    </TopToolbar>
);

export const PointConceptList = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    return (
        <RaList
            queryOptions={options}
            actions={<PointConceptListActions />}
            perPage={20}
            pagination={<Pagination rowsPerPageOptions={[10, 20, 30, 100]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
        >
            <PointConceptListContent />
        </RaList>
    )
};

