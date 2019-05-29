import React from "react";
import PropTypes from 'prop-types';
import Header from './components/Header'
export class App extends React.Component {
    
    render() {
        return(

<div> <Header title="The React Product" />
   
                    {this.props.children}
                </div>
  
        )
        
    }
}