import React from 'react';
import Categories from './Categories'

class ProductForm extends React.Component {

	constructor() {
		super();
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleSubmit(event) {
		event.preventDefault();
		const data = new FormData(event.target);

		var url = 'http://localhost:9000/addproduct';

		fetch(url, {
			method: 'POST',
			body: data,
		});
	}

	render() {
		return (
			<form onSubmit={this.handleSubmit}>
				<label htmlFor="name">Product Name</label>
				<input id="name" name="name" type="text" />
				<label htmlFor="description">Description</label>
				<input id="description" name="description" type="text" />
				<label htmlFor="category">Category</label>
				<Categories source="1"/>
				<button type="submit">Add</button>
			</form>
		);
	}
}

export default ProductForm;
