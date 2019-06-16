import React from 'react';
import Product from './Product'
import User from './User'

class Baskets extends React.Component
{
	constructor()
	{
		super();
		this.state = {baskets: []};
	}

	componentDidMount()
	{
		var url = "http://localhost:9000/baskets";

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
						if (this.props.source)
							return (
								<option key={basket.id} value={basket.id}><Product id={basket.product} source="1"/>{basket.quantity}</option>
							);
						else
							return (
								<tr key={basket.id}>
									<td><User id={basket.user}/></td>
									<td><Product id={basket.product}/></td>
									<td>{basket.quantity}</td>
									<td>{basket.status}</td>
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
		if (this.props.source)
			return (
				<select id="basket" name="basket">
				{this.state.baskets}
				</select>
		);
		else
			return (
				<table className="baskets">
					{this.state.baskets}
				</table>
		);
	}
}

export default Baskets;
