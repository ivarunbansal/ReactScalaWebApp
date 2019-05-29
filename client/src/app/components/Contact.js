
import React, {PureComponent} from "react";
import PropTypes from "prop-types";
import Address from "./Address"
export default class Contact extends PureComponent {
    
    componentWillMount(){

    }
    
    componentDidMount() {
        
    }
    
    render() {

        let branchoffice={
            "addressline1":"612 Park Centra,",
            "addressline2":" Sector30",
            "city":"Gurgaon ",
            "pincode":"122002",
            "state":"Haryana",
            "phone":"+91 (0124) 470 0200",
            "email":"infoindia@xebia.com"
        }
        
        let headoffice={
            "addressline1":"3rdFloor Wing A, Indiqube Alpha Building",
            "addressline2":"Panathur Junction, Marathahalli, Sarjapur Outer Ring Road",
            "city":"Bangalore",
            "pincode":"560103",
            "state":"Karnataka",
            "phone":"+ 91 080 4662 2200",
            "email":"infoindia@xebia.com"
        }

        return (
            <div id="contact"> 
            <h2>Contact Us</h2>




  <Address branchoffice={branchoffice}  headoffice={headoffice} />
            </div>
        )
    }
} 


Contact.defaultProps = {
    
}

Contact.propTypes = {
    
}

