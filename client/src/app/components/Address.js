import React, { PureComponent } from "react";
import PropTypes from "prop-types";

export default class Address extends PureComponent {

    constructor(props) {

        super(props);
    }

    render() {

        return (

            <div id="address">
                <table><tbody>
                    <tr>
                        <th width="10%">
                            Branch Office
</th>

                        <td width="20%" height="100px">
                          

                            <p className="address"> {this.props.branchoffice.addressline1}  {this.props.branchoffice.addressline2}</p>
                            <p className="address">{this.props.branchoffice.city}  {this.props.branchoffice.pincode}</p>
                            <p className="address"> {this.props.branchoffice.state}</p>
                            <p className="address">{this.props.branchoffice.phone}</p>
                            <p className="address">{this.props.branchoffice.email}</p>
                        </td>
                    </tr>



                    <tr>
                        <th width="20%">
                            Head Office
</th>

                        <td width="20%" height="200px">
                        


                            <p className="address"> {this.props.headoffice.addressline1}  {this.props.headoffice.addressline2}</p>
                            <p className="address">{this.props.headoffice.city}  {this.props.headoffice.pincode}</p>
                            <p className="address"> {this.props.headoffice.state}</p>
                            <p className="address">{this.props.headoffice.phone}</p>
                            <p className="address">{this.props.headoffice.email}</p>




                        </td>
                    </tr>

                </tbody>
                </table>
            </div>

        )
    }



}
Address.defaultProps = {

}

Address.propTypes = {

}
