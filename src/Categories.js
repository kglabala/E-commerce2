import React from 'react';

class Categories extends React.Component
{
    constructor(props)
    {
        super();
        this.state = {categories: []};
    }

    componentDidMount()
    {
        var url = "http://localhost:9000/categories";

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
                let categories = data.map(
                        (category) => {
                            return (
								<option value={category.id}>{category.name}</option>
/*                                <div key={category.id}>
                                    <div className="title">{category.name}</div>
                                </div> */
                            );
                        }
                    )
                this.setState({categories: categories});
            }
        )
    }

    render()
    {
		if (this.props.source)
			return (
				<select id="category" name="category">
					{this.state.categories}
				</select>
			);
		else
			return (
				<div>
					{this.state.categories}
				</div>
			);
	}
}

export default Categories;
