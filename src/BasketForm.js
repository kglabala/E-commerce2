import React from 'react';
import Products from './Products'
import Users from './Users'

class BasketForm extends React.Component {

	constructor() {
		super();
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleSubmit(event) {
		event.preventDefault();
		const data = new FormData(event.target);

		var url = 'http://localhost:9000/addbasket';

		fetch(url, {
			method: 'POST',
			body: data,
		});
	}

	render() {
		return (
			<form onSubmit={this.handleSubmit}>
				<label htmlFor="name">Basket User</label>
				<Users source="1"/>
				<label htmlFor="category">Product</label>
				<Products source="1"/>
				<label htmlFor="quantity">Quantity</label>
				<input id="quantity" name="quantity" type="text"/>
				<label htmlFor="status" style={{display: "none"}}>Status</label>
				<input id="status" name="status" type="number" value="1" style={{display: "none"}}/>
				<button type="submit">Add</button>
			</form>
		);
	}
}

export default BasketForm;
