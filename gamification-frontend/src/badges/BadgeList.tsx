import {
    List as RaList,
    SimpleListLoading,
    useListContext, TopToolbar,
    CreateButton,
    Pagination, Datagrid,
    TextField,
    useStore
} from 'react-admin';
import { Badge } from '../types';
import { BulkActionButtons } from '../misc/BulkActionButtons';

const BadgesListContent = () => {
    const {
        data: Badges,
        isLoading,
        onToggleItem,
        selectedIds,
    } = useListContext<Badge>();
    if (isLoading) {
        return <SimpleListLoading hasLeftAvatarOrIcon hasSecondaryText />;
    }
    const now = Date.now();
    return (
        <>
            <Datagrid bulkActionButtons={<BulkActionButtons title="Badge" resource="badges" />} sx={{
                '& .RaDatagrid-headerCell': {
                    fontWeight: 600
                },
            }}>
                <TextField label="Badge Name" source="id" />
                <TextField label="Hidden" source="hidden" />
            </Datagrid>

        </>
    );
};

const BadgeListBadges = () => (
    <TopToolbar>
        <CreateButton
            variant="contained"
            label="New Badge"
            sx={{ marginLeft: 2 }}
        />
    </TopToolbar>
);

export const BadgeList = () => {
    const [gameId] = useStore('game.selected');
    const options = { meta: { gameId: gameId } };
    return (
        <RaList
            queryOptions={options}
            actions={<BadgeListBadges />}
            perPage={20}
            pagination={<Pagination rowsPerPageOptions={[10, 20, 30, 40]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
        >
            <BadgesListContent />
        </RaList>
    )
};


