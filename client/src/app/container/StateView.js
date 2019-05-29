import { connect } from "react-redux";
import stateview from "../components/StateView";

import * as action from "../state/actions";
const mapStateToProps = (state) => {
   
    return {

        state: state.stateview.state,
        status: state.stateview.status,
        error: state.stateview.error

    }
}

const mapDispatchToProps = (dispatch) => {
   
    return {
        getStateById: function (id) {
            let actionFn = action.getStateById(id);
            dispatch(actionFn);
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(stateview)