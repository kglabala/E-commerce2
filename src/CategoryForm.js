import React from 'react';

class CategoryForm extends React.Component {

    constructor() {
        super();
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
        event.preventDefault();
        const data = new FormData(event.target);

        var url = 'http://localhost:9000/addcategory';

        fetch(url, {
            method: 'POST',
            body: data,
        });
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label htmlFor="name">Category Name</label>
                <input id="name" name="name" type="text" />
                <button>Add</button>
            </form>
        );
    }

}

export default CategoryForm;
