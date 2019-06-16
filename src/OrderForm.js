import React from 'react';
import Baskets from './Baskets'
import Users from './Users'

class OrderForm extends React.Component {

	constructor() {
		super();
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleSubmit(event) {
		event.preventDefault();
		const data = new FormData(event.target);

		var url = 'http://localhost:9000/addorder';

		fetch(url, {
			method: 'POST',
			body: data,
		});
	}

	render() {
		return (
			<form onSubmit={this.handleSubmit}>
				<label htmlFor="name">Order User</label>
				<Users source="1"/>
				<label htmlFor="basket">Basket</label>
				<Baskets source="1"/>
				<label htmlFor="address">Address</label>
				<input id="address" name="address" type="text"/>
				<label htmlFor="status" style={{display: "none"}}>Status</label>
				<input id="status" name="status" type="number" value="1" style={{display: "none"}}/>
				<button type="submit">Add</button>
			</form>
		);
	}
}

export default OrderForm;
