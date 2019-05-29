import * as ActionTypes from "./action-types";

import * as service from "./service";

export function initStates(states){
    return {

        type: ActionTypes.INIT_STATES,
        payload: {
            states: states
        }
    }
}
export function initStateView(state){
    return {

        type: ActionTypes.INIT_STATEVIEW,
        payload: {
            state: state
        }
    }
}


export function initStateUpdate(state){
  
    return {

        type: ActionTypes.INIT_STATEUPDATE,
        payload: {
            state: state
        }
    }
}


export function loading(status){
    return {
        type: ActionTypes.LOADING,
        payload: {
            status: status
        }
    }
}


export function initError(error) {
    return {
        type: ActionTypes.ERROR,
        payload: {
            error: error
        }
    }
}

export function fetchStates() {
    
    return function(dispatch, getState) {
     
    
        dispatch(loading(true));
        service.getStates()
        .then (states => {
            let action = initStates(states);
            dispatch(action);
            dispatch(loading(false));
        });

    }
}


export function saveUpdatedState(state) {
    
    return function(dispatch, getState) {
     
        dispatch(loading(true));
        service.updateState(state)
        .then (state => {
            let action = initStateView(state);
            dispatch(action);
            dispatch(loading(false));
        });

    }
}

export function createState(state) {
    
    return function(dispatch, getState) {
     
        dispatch(loading(true));
        service.createState(state)
        .then (state => {
            let action = initStateView(state);
            dispatch(action);
            dispatch(loading(false));
        });

    }
}

export function getUpdatedState(id) {
    
    return function(dispatch, getState) {
     
        dispatch(loading(true));
        service.getStateById(id)
        .then (state => {
            let action = initStateUpdate(state);
            dispatch(action);
            dispatch(loading(false));
        });

    }
}




export function getStateById(id) {
    
    return function(dispatch, getState) {
     
    
        dispatch(loading(true));
        service.getStateById(id)
        .then (state => {
            let action = initStateView(state);
            dispatch(action);
            dispatch(loading(false));
        });

    }
}