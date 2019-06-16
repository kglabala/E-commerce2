import React from 'react';

class UserForm extends React.Component {

	constructor() {
		super();
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleSubmit(event) {
		event.preventDefault();
		const data = new FormData(event.target);

		var url = 'http://localhost:9000/adduser';

		fetch(url, {
			method: 'POST',
			body: data,
		});
	}

	render() {
		return (
			<form onSubmit={this.handleSubmit}>
				<label htmlFor="name">First Name</label>
				<input id="name" name="name" type="text" />
				<label htmlFor="surname" type="text">Last Name</label>
				<input id="surname" name="surname" type="text" />
				<label htmlFor="email">E-Mail</label>
				<input id="email" name="email" type="text" />
				<label htmlFor="address">Address</label>
				<input id="address" name="address" type="text" />
				<label htmlFor="token" style={{display: 'none'}}>Token</label>
				<input id="token" name="token" type="text" style={{display: 'none'}} />
				<button type="submit">Add</button>
			</form>
		);
	}
}

export default UserForm;
