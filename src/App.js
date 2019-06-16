import React from 'react';
import { BrowserRouter, Link, Route } from 'react-router-dom';
import './App.css';
import Baskets from './Baskets'
import BasketForm from './BasketForm'
import Categories from './Categories'
import CategoryForm from './CategoryForm'
import Orders from './Orders'
import OrderForm from './OrderForm'
import Products from './Products'
import ProductForm from './ProductForm'
import Users from './Users'
import UserForm from './UserForm'

class App extends React.Component {
	render() {
		return (
			<BrowserRouter>
				<div id="menu">
					<ul>
						<li><Link to="/baskets">Baskets</Link></li>
						<li><Link to="/basketadd">Add Basket</Link></li>
						<li><Link to="/categories">Categories</Link></li>
						<li><Link to="/categoryadd">Add Category</Link></li>
						<li><Link to="/orders">Orders</Link></li>
						<li><Link to="/orderadd">Add Order</Link></li>
						<li><Link to="/products">Products</Link></li>
						<li><Link to="/productadd">Add Product</Link></li>
						<li><Link to="/users">Users</Link></li>
						<li><Link to="/useradd">Add User</Link></li>
					</ul>
					<Route path="/baskets" component={Baskets} />
					<Route path="/basketadd" component={BasketForm} />
					<Route path="/categories" component={Categories} />
					<Route path="/categoryadd" component={CategoryForm} />
					<Route path="/orders" component={Orders} />
					<Route path="/orderadd" component={OrderForm} />
					<Route path="/products" component={Products} />
					<Route path="/productadd" component={ProductForm} />
					<Route path="/users" component={Users} />
					<Route path="/useradd" component={UserForm} />
				</div>
			</BrowserRouter>
		);
	}
}

export default App;
