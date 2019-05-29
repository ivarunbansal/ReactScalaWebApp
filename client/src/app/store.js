import {createStore,combineReducers,applyMiddleware} from 'redux';
import statereducer from "./state/statereducer";
import stateviewreducer from "./state/stateviewreducer";
import stateeditreducer from "./state/stateeditreducer";
import thunk from 'redux-thunk';
function loggerMiddleware(store){

    return function(next){
        return function(action)
        {
        
            let result=next(action);
            return result;
        }
    }
}


const rootReducers=combineReducers({
    stateview:stateviewreducer,
    mystates:statereducer,
    stateupdate:stateeditreducer
});

let store =createStore(rootReducers,applyMiddleware(loggerMiddleware,thunk));


export default store;