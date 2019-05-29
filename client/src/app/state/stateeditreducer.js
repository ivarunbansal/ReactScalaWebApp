import * as ActionTypes from '../state/action-types';

const INITIAL_STATE = {
    state: [],
    status: false,
    error: undefined
}
export default function stateeditreducer(state = INITIAL_STATE, action) {
    switch(action.type) {
        case ActionTypes.INIT_STATEUPDATE:
            return Object.assign({}, state, {state: action.payload.state});

        case ActionTypes.LOADING:
            return Object.assign({}, state, {status: action.payload.status});

        case ActionTypes.ERROR:
            return Object.assign({}, state, {error: action.payload.error});

        default:
            return state;
    }
}