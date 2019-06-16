import React from 'react';

class Products extends React.Component
{
    constructor()
    {
        super();
        this.state = {products: []};
    }

    componentDidMount()
    {
        var url = "http://localhost:9000/products";

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
                                    <option key={product.id} value={product.id}>{product.name}</option>
                                );
                            else
                                return (
                                    <tr key={product.id}>
                                        <td className="title">{product.name}</td>
                                        <td>{product.description}</td>
                                        <td>{product.category}</td>
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
                <select id="product" name="product">
                    {this.state.products}
                </select>
            );
        else
            return (
                <table className="products">
                    {this.state.products}
                </table>
            );
    }
}

export default Products;
