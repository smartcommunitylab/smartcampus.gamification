import {
    ShowBase,
    Tab,
    TabbedShowLayout,
    useRefresh,
    useShowContext
} from 'react-admin';
import { Game } from '../types';


export const GameShow = () => (
    <ShowBase>
        <GameShowContent />
    </ShowBase>
);

const GameShowContent = () => {
    
    const { record, isLoading } = useShowContext<Game>();
    if (isLoading || !record) return null;
    
    return (
        <TabbedShowLayout>
            {/* <Tab label="Points">
                <PointConceptTab />
            </Tab>
            <Tab label="BadgeCollection">
                <BadgeCollectionTab />
            </Tab>
            <Tab label="Challenge Models">
                <ChallengeModelTab />
            </Tab>
            <Tab label="Actions">
                <ActionTab />
            </Tab>
            <Tab label="Rules">
                <RuleTab />
            </Tab>
            <Tab label="Levels">
                <LevelTab />
            </Tab>
            <Tab label="Task">
                <TaskTab />
            </Tab>
            <Tab label="Monitor">
                <MonitorTab />
            </Tab> */}
        </TabbedShowLayout>
    );
};





