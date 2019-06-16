import React from 'react';

class User extends React.Component
{
    constructor()
    {
        super();
        this.state = {users: []};
    }

    componentDidMount()
    {
        var url = "http://localhost:9000/user/" + this.props.id;

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
                let users = data.map(
                        (user) => {
                            return (
                                <tr key={user.id}>
                                    <td className="title">{user.name}</td>
                                </tr>
                            );
                        }
                    )
                this.setState({users: users});
            }
        )
    }

    render()
    {
        return (
            <table className="user">
                {this.state.users}
            </table>
        );
    }
}

export default User;
