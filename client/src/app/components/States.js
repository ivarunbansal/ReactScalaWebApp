import React, {Component} from "react";
import PropTypes from "prop-types";
import {NavLink} from 'react-router-dom';
export default class States extends Component{

    constructor(props) {
        super(props);
    }
    componentDidMount() {
        this.props.fetchStates();
    }

    refresh() {
        this.props.fetchStates();
    }



    render() {
        let states = this.props.states || [];
        let status = this.props.status;
        let error = this.props.error;
if(status){
return(

    <div id="statesloader">
    <h2>Loading .....</h2>
    <img src="/assets/loading.gif" />
</div>

)
}
return (
    <div id ="states"> 
    <h2>STATES </h2>
    <NavLink to={`/states/create`}  ><button className="updatebutton">Add New State</button></NavLink>
    {/* <button onClick={() => this.refresh()}>
      Refresh
    </button> */}


 <table border="0px">
 <thead>
<tr>
    <th>
        State Id
        </th> 
        <th>
            State Code
            </th> 
            <th>
State Name
            </th>
            <th>View</th>
            <th>
                Edit
            </th>
    </tr>

 </thead>
                <tbody>
   
        {
            states.map( state => (
            <tr key ={state.id}>
            <td> {state.id}</td>
<td> {state.code}</td>

            <td>
                
                  <NavLink to={`/states/view/${state.id}`}  >  {state.name}</NavLink>
               
                </td>
                <td>
                
                <NavLink to={`/states/view/${state.id}`}  > <img src="../../assets/view2.png" /></NavLink>
             
              </td>
<td>
            <NavLink to={`/states/edit/${state.id}`}  >   <img src="../../assets/Edit.png" /></NavLink>
</td>

                </tr>
            ))
        }
  
    </tbody>
    </table>

    </div>
)

}


    }


States.defaultProps = {
    
}

States.propTypes = {
    
}