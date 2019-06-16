import React from 'react';
import User from "./User";
import Product from "./Product";

class Basket extends React.Component
{
    constructor()
    {
        super();
        this.state = {baskets: []};
    }

    componentDidMount()
    {
        var url = "http://localhost:9000/basket/" + this.props.id;

        fetch(
            url,
            {
                    mode: 'cors',
                    headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin':'http://localhost:3000'},
                    method: 'GET',
                }
        ).then(
            results =>
            {
                return results.json();
            }
        ).then(
            data =>
            {
                let baskets = data.map(
                    (basket) => {
                        return (
                            <tr key={basket.id}>
                                <td><User id={basket.user}/></td>
                                <td><Product id={basket.product}/></td>
                                <td>{basket.quantity}</td>
                            </tr>
                        );
                    }
                )

                this.setState({baskets: baskets});
            }
        )
    }

    render()
    {
        return (
            <table className="basket">
                {this.state.baskets}
            </table>
        );
    }
}

export default Basket;
