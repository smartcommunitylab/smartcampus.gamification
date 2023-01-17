import {
    List as RaList,
    SimpleListLoading,
    useListContext, TopToolbar,
    CreateButton,
    Pagination,
    BulkActionsToolbar,
    BulkDeleteButton,
    RecordContextProvider,
    TextInput,
    SearchInput
} from 'react-admin';
import { useRedirect } from 'react-admin';
import {
    List,
    ListItem,
    ListItemText,
    Button
} from '@mui/material';
import CheckCircleOutlinedIcon from '@mui/icons-material/CheckCircleOutlined';
import SettingsIcon from '@mui/icons-material/Settings';
import { Game } from '../types';

const GameListContent = () => {
    const {
        data: Games,
        isLoading,
        onToggleItem,
        selectedIds,
    } = useListContext<Game>();
    if (isLoading) {
        return <SimpleListLoading hasLeftAvatarOrIcon hasSecondaryText />;
    }
    const now = Date.now();

    return (
        <>
            <BulkActionsToolbar>
                <BulkDeleteButton />
            </BulkActionsToolbar>
            <List>
                {Games.map(Game => (
                    <RecordContextProvider key={Game.id} value={Game}>
                        <ListItem>
                            <ListItemText primary={`${Game.name}`} secondary={`${Game.id}`}></ListItemText>
                            <SettingButton selectedId={Game.id} />
                            <ManageButton selectedId={Game.id} selectedName={Game.name}></ManageButton>
                        </ListItem>
                    </RecordContextProvider>
                ))}
            </List>
        </>
    );
};

const GameListActions = () => (
    <TopToolbar>
        {/* <SortButton fields={['name', 'id']} /> */}
        {/* <ExportButton /> */}
        <CreateButton
            variant="contained"
            label="New Game"
            sx={{ marginLeft: 2 }}
        />
    </TopToolbar>
);

export const GameList = () => {
    return (
        <RaList
            actions={<GameListActions />}
            perPage={25}
            pagination={<Pagination rowsPerPageOptions={[10, 25, 50, 100]} />}
            sort={{ field: 'last_seen', order: 'DESC' }}
            filters={GameFilters}
        >
            <GameListContent />
        </RaList>
    )
};

import { useStore } from 'react-admin';

const ManageButton = (params: any) => {
    const [gameId, setGameId] = useStore('game.selected');
    const [gameName, setGameName] = useStore('game.name');
    return (
        <>
            <Button endIcon={<CheckCircleOutlinedIcon />} onClick={() => {
                setGameId(params.selectedId);
                setGameName(params.selectedName);
                console.log(params.selectedId);
            }}>
                Manage
            </Button>
        </>
    );

};

const SettingButton = (params: any) => {
    const redirect = useRedirect();
    const [gameId, getGameId] = useStore('game.selected');
    return (
        <>
            <Button disabled={params.selectedId != gameId} endIcon={<SettingsIcon />} onClick={() => {
                redirect('/game/' + params.selectedId + '/edit');
            }}>
                Edit Settings
            </Button>
        </>
    );
}

const GameFilters = [
    <SearchInput placeholder='Search by game name'source="q" alwaysOn />
];