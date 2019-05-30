
import { connect } from "react-redux";

import statelist from "../components/States";
import * as action from "../state/actions";
import { fileURLToPath } from "url";

const mapStateToProps = (state) => {


    return {

        states: state.mystates.states,
        status: state.mystates.status,
        error: state.mystates.error

    }
}

const mapDispatchToProps = (dispatch) => {
  
    return {
        fetchStates: function () {
            let actionFn = action.fetchStates();
            dispatch(actionFn);
        },deleteState:function(id){
             let deleteActionFunction=action.deleteState(id);
            dispatch(deleteActionFunction);
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(statelist)