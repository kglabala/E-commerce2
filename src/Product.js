import React from 'react';

class Product extends React.Component
{
    constructor()
    {
        super();
        this.state = {products: []};
    }

    componentDidMount()
    {
        var url = "http://localhost:9000/product/" + this.props.id;

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
                let products = data.map(
                        (product) => {
                            if (this.props.source)
                                return (
                                    <span>{product.name}</span>
                                );
                            else
                                return (
                                    <tr key={product.id}>
                                        <td className="title">{product.name}</td>
                                    </tr>
                                );
                        }
                    )
                this.setState({products: products});
            }
        )
    }

    render()
    {
        if (this.props.source)
            return (
                <span>
                    {this.state.products}
                </span>
            );
        else
            return (
                <table className="product">
                    {this.state.products}
                </table>
            );
    }
}

export default Product;
