import { connect } from "react-redux";
import stateedit from "../components/StateEdit";

import * as action from "../state/actions";

const mapStateToProps = (state) => {
   
    return {

        state: state.stateupdate.state,
        status: state.stateupdate.status,
        error: state.stateupdate.error

    }
}

const mapDispatchToProps = (dispatch) => {
   
    return {

        updateState: function (updatedState) {
            let actionFn = action.initStateUpdate(updatedState);
            dispatch(actionFn)
        },

        saveUpdatedState: function (state) {
            let actionFn = action.saveUpdatedState(state);
            dispatch(actionFn)
        },
      
        createState: function (state) {
            let actionFn = action.createState(state);
            dispatch(actionFn)
        },


        getStateById: function (id) {
            let actionFn = action.getUpdatedState(id);
            dispatch(actionFn);
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(stateedit)