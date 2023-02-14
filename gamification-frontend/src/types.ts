import { RaRecord, Identifier } from 'react-admin';

export interface Game extends RaRecord {
    name: string;
    id: string;
}

export interface Badge extends RaRecord {
    id: string;
}

export interface Action extends RaRecord {
    id: string;
}

export interface Task extends RaRecord {
    id: string;
    name: string;
    type: string;
    cronExpression: string;
    itemsToNotificate: number;
    itemType: string;
    classificationName: string;
    periodName: string;
    periodLength: number;
    periodUnit: string;
}

export interface PointConcept extends RaRecord {
    id: string;
    pc: any;
}

export interface Rule extends RaRecord {
    id: string;
    name: string;
    content: string,
    show: boolean,
    editing: boolean,
}

export interface ThresholdConfig extends RaRecord {
    choices: number;
    availableModels: string[];
    activeModels: string[];
}

export interface Threshold extends RaRecord {
    name: string;
    value: number;
    config: ThresholdConfig[];
}

export interface Level extends RaRecord {
    name: string;
    thresholds: Threshold[];
    pointConcept: string;
}

export interface ChallengeModel extends RaRecord {
    name: string;
    variables: string[];
}

