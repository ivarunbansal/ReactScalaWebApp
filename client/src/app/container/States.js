
import { connect } from "react-redux";

import statelist from "../components/States";
import * as action from "../state/actions";

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
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(statelist)