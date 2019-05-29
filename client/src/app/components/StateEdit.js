import React, { PureComponent } from "react";
import PropTypes from "prop-types";

export default class StateEdit extends PureComponent {


    constructor(props) {

        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }


    handleChange(e) {


        let { name, value } = e.target;
        let mystate = Object.assign({},
            this.props.state,
            { [name]: value });

        this.props.updateState(mystate);

    }

    handleSubmit(e) {
        e.preventDefault();
        let id = this.props.match.params.id;

        if (id) {
            this.props.saveUpdatedState(this.props.state);
            alert("state " + this.props.state.id + " successfully updated");
        }
        else {
            this.props.createState(this.props.state);
            alert("state successfully created");
        }


    }

    componentWillMount() {


        let id = this.props.match.params.id;

        if (id) {
            this.props.getStateById(id);
        }



    }


    render() {

        let mystate = this.props.state || [];
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

            <div >
                {
                    this.props.match.params.id && <div id="stateedit">
                        <h2>Update State</h2>
                        <form onSubmit={(e) => this.handleSubmit(e)}>
                            <table>
                                <tbody>

                                    <tr>
                                        <td><b>State Id</b></td>
                                        <td>{mystate.id}</td></tr>

                                    <tr>
                                        <td><b>State Code</b> </td>
                                        <td>
                                            <input name="code" onChange={(e) => this.handleChange(e)} value={mystate.code} required />
                                        </td></tr>

                                    <tr>
                                        <td><b>State Name</b></td>
                                        <td>
                                            <input name="name" onChange={(e) => this.handleChange(e)} value={mystate.name} required/>

                                        </td></tr>

                                    <input className="updatebutton" type="submit" value="Update" />

                                </tbody>
                            </table>
                        </form>
                    </div>
                }


                {
                    !this.props.match.params.id && <div id="statecreate">
                        <h2>Create State</h2>
                        <form onSubmit={(e) => this.handleSubmit(e)}>
                            <table>
                                <tbody>


                                    <tr>
                                        <td><b>State Code</b> </td>
                                        <td>
                                            <input name="code" onChange={(e) => this.handleChange(e)} required />
                                        </td></tr>

                                    <tr>
                                        <td><b>State Name</b></td>
                                        <td>
                                            <input name="name" onChange={(e) => this.handleChange(e)}  required/>

                                        </td></tr>



                                    <input className="updatebutton" type="submit" value="Create" />

                                </tbody>
                            </table>
                        </form>
                    </div>
                }


            </div>


        )




    }


}


StateEdit.defaultProps = {
  


}

StateEdit.propTypes = {

}





