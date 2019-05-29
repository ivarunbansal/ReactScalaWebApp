import React, {PureComponent} from "react";
import PropTypes from "prop-types";
import {NavLink} from 'react-router-dom';
export default class Header extends PureComponent{
    constructor() {
        super(); 
        console.log("Header created");
    }
   
    static propTypes = {
        title: PropTypes.string.isRequired
    }
    render(){

        return (
            <div id ="headerdiv">
              

                <h2>{this.props.title}</h2>
<ul id ="links">
                <NavLink to="/"   className="button" activeClassName="success" >
                     Home
                </NavLink>

                <NavLink to="/about" className="button" activeClassName="success">
                     About
                </NavLink>


                <NavLink to="/contact" className="button" activeClassName="success">
                     Contact
                </NavLink>
                  
                <NavLink to="/states" className="button" activeClassName="success">
                     States
                </NavLink>
                </ul>
            </div>
        ) 
    }
}