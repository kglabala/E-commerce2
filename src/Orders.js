import React from 'react';
import Basket from './Basket'
import User from './User'

class Orders extends React.Component
{
    constructor()
    {
        super();
        this.state = {orders: []};
    }

    componentDidMount()
    {
        var url = "http://localhost:9000/orders";

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
                let orders = data.map(
                        (order) => {
                            return (
                                <tr key={order.id}>
                                    <td><User id={order.user}/></td>
                                    <td><Basket id={order.basket}/></td>
                                    <td>{order.address}</td>
                                </tr>
                            );
                        }
                    )
                this.setState({orders: orders});
            }
        )
    }

    render()
    {
        return (
            <table className="orders">
                {this.state.orders}
            </table>
        );
    }
}

export default Orders;
