import React from 'react';

class Users extends React.Component
{
    constructor()
    {
        super();
        this.state = {users: []};
    }

    componentDidMount()
    {
        var url = "http://localhost:9000/users";

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
                            if (this.props.source)
                                return (
                                    <option key={user.id} value={user.id}>{user.email}</option>
                                );
                            else
                                return (
                                    <tr key={user.id}>
                                        <td className="title">{user.name}</td>
                                        <td>{user.surname}</td>
                                        <td>{user.email}</td>
                                        <td>{user.address}</td>
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
        if (this.props.source)
            return (
                <select id="user" name="user">
                    {this.state.users}
                </select>
            );
        else
            return (
                <table className="users">
                    {this.state.users}
                </table>
            );
    }
}

export default Users;
