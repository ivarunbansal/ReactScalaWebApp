import * as ActionTypes from '../state/action-types';

const INITIAL_STATE = {
    states: [],
    status: false,
    error: undefined
}
export default function statereducer(state = INITIAL_STATE, action) {
    switch(action.type) {
        case ActionTypes.INIT_STATES:
            return Object.assign({}, state, {states: action.payload.states});

        case ActionTypes.LOADING:
            return Object.assign({}, state, {status: action.payload.status});

        case ActionTypes.ERROR:
            return Object.assign({}, state, {error: action.payload.error});
            
        default:
            return state;
    }
}