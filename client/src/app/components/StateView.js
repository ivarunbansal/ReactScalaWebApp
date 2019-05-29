import React, { PureComponent } from "react";
import PropTypes from "prop-types";

export default class StateView extends PureComponent {

    constructor(props) {

        super(props);
    }


    componentDidMount() {


        let id = this.props.match.params.id;

        if (id) {
            this.props.getStateById(id);
        }

    }

    render() {

        let state = this.props.state || [];
        let status = this.props.status;
        let error = this.props.error;



        if (status) {
            return (

                <div id="statesloader">
                    <h2>Loading .....</h2>
                    <img src="/assets/loading.gif" />
                </div>

            )
        }

        return (
            <div id="stateview">
                <h2>State View</h2>

                <table border="0px">
                    <tbody>

                        <tr><td><b>ID</b></td><td>{state.id}</td></tr>
                        <tr><td><b>CODE</b></td><td>{state.code}</td></tr>
                        <tr><td><b>NAME</b></td><td> {state.name}</td></tr>

                    </tbody>
                </table>

            </div>
        )


    }





}
StateView.defaultProps = {
    
}

StateView.propTypes = {
    
}